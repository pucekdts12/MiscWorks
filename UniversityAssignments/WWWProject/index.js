'use strict'
const express = require('express')
const ejs = require('ejs')
const fs = require('fs')
const fileUpload = require('express-fileupload')
const mime = require('mime-types')
// const dts = require('./src/dtslib.js')
let app = express()


app.engine('html',ejs.__express)
app.set('view engine','html')
app.set('views',__dirname+'/html')

app.use('/css',express.static('css'))
app.use('/js',express.static('js'))
app.use('/fonts',express.static('fonts'))
app.use('/img',express.static('img'))

app.use(fileUpload({
  createParentPath: true,
  limits:{fileSize:10*1024*1024} // 10MB
}))

app.use(express.urlencoded({"extended":true}))



app.use(function(rq,rs,next){
  rs.locals._GET=rq.query
  rs.locals._POST=rq.body
  next()
})

const db = require('better-sqlite3')(__dirname+'/db/movies.db');


let get_tags = db.prepare('SELECT name FROM tags INNER JOIN movie_tag ON tag_id=tags.id WHERE movie_id=? LIMIT 3')
get_tags.pluck(true)


app.get('/',function(rq,rs){
  let movies = db.prepare('SELECT movies.id,title,year,rating,poster,type FROM movies INNER JOIN movie_info ON movies.id=movie_info.id ORDER BY title').all()
  movies.forEach((movie)=>{
    movie.genres = get_tags.all(movie.id)
    if (Number.isNaN(parseFloat(movie.rating))) movie.rating = 0
  })

  rs.render('index',{"movies":movies})
})

app.get('/advancedSearch',function(rq,rs){
  let genres = db.prepare('SELECT id,name FROM tags').all()
  let types = db.prepare("SELECT id,name,(SELECT CASE WHEN id=0 THEN 'checked' ELSE '' END) as checked FROM types").all()
  rs.render('advancedSearch',{'genres':genres,'types':types})
})

app.get('/edit/:id?',function(rq,rs){
  let genres
  let movie={'id':-1,'title':'','year':'','rating':0,'poster':''/*blank.jpg*/}
  let movie_id = rq.params.id
  if( movie_id && movie_id!=-1 ){
    genres = db.prepare('SELECT id,name,(SELECT COUNT(*) FROM movie_tag WHERE movie_id=? AND tag_id=tags.id  ) as checked FROM tags ORDER BY name').all(movie_id)
    movie = db.prepare('SELECT movies.id,title,year,rating,poster,type FROM movies INNER JOIN movie_info ON movie_info.id=movies.id WHERE movies.id=?').get(movie_id)
  }else{
    genres = db.prepare('SELECT id,name,0 as checked FROM tags ORDER BY name').all()
  }
  let types = db.prepare('SELECT id,name FROM types').all()
  if (Number.isNaN(parseFloat(movie.rating))) movie.rating = 0
  rs.render('movie_edit',{'genres':genres,'movie':movie,'types':types})
})

app.get('/info/:id',function(rq,rs){
  let genres
  let movie={'id':-1,'title':'','year':'','rating':0,'poster':''/*blank.jpg*/}
  let movie_id = rq.params.id
  if( movie_id && movie_id!=-1 ){
    genres = db.prepare('SELECT name FROM tags INNER JOIN movie_tag ON tag_id=id WHERE movie_id=?').pluck(true).all(movie_id)
    movie = db.prepare('SELECT movies.id,title,year,rating,poster,types.name as type FROM movies INNER JOIN movie_info ON movie_info.id=movies.id INNER JOIN types ON types.id=movie_info.type WHERE movies.id=?').get(movie_id)
  }
  if (Number.isNaN(parseFloat(movie.rating))) movie.rating = 0
  rs.render('movie_info.html',{'genres':genres,'movie':movie})
})

app.get('/admin',function(rq,rs){
  let movies = db.prepare('SELECT movies.id,title,year,rating,poster,type FROM movies INNER JOIN movie_info ON movies.id=movie_info.id ORDER BY title').all()
  movies.forEach((movie)=>{
    movie.genres = get_tags.all(movie.id)
    if (Number.isNaN(parseFloat(movie.rating))) movie.rating = 0
  })

  rs.render('admin_panel.html',{"movies":movies})
})

app.post('/edit',async function(rq,rs){
  try{
    if( rq.files && rq.files.posterfile){
      // rq.body.poster = 'img/'+(rq.body.title.replace(/ /g,''))+'.'+mime.extension(rq.files.posterfile.mimetype)
      rq.body.poster = 'img/'+(rq.body.title.replace(/[/\\?%*:|"<>\ ]/g,''))+'.'+mime.extension(rq.files.posterfile.mimetype)
    }else{
      rq.body.poster=''
    }
    let movie_id = db.prepare('SELECT id FROM movies WHERE id=?').pluck(true).get(rq.body.id);
    if ( movie_id==undefined ){ // INSERT
      let insert_movie = db.transaction(()=>{
        db.prepare('INSERT INTO movies(title) VALUES(?)').run(rq.body.title)
        movie_id = db.prepare('SELECT id FROM movies WHERE title=?').get(rq.body.title).id
        rq.body.id = movie_id
        let insert_tag = db.prepare('INSERT INTO movie_tag VALUES(?,?)')
        for( const genre of rq.body.genres){
          insert_tag.run(movie_id,genre)
        }

        db.prepare('INSERT INTO movie_info(id,year,rating,poster,type) VALUES(@id,@year,@rating,@poster,@type)').run(rq.body)
        if( rq.files && rq.files.posterfile){
          rq.files.posterfile.mv(__dirname+'/'+rq.body.poster,function(err){
            if(err)
              throw 'File Upload Failed'
          })
        }
      })

      insert_movie()
    } else {
      let update_movie = db.transaction(()=>{
        rq.body.id = movie_id
        db.prepare('UPDATE movie_info SET year=@year,rating=@rating,type=@type WHERE id=@id').run(rq.body)
        db.prepare('UPDATE movies SET title=@title WHERE id=@id').run(rq.body)
        db.prepare('DELETE FROM movie_tag WHERE movie_id=@id').run(rq.body)
        let insert_tag = db.prepare('INSERT INTO movie_tag VALUES(?,?)')
        for( const genre of rq.body.genres){
          insert_tag.run(movie_id,genre)
        }

      })

      if( rq.files && rq.files.posterfile){
        db.prepare('UPDATE movie_info SET poster=@poster WHERE id=@id').run(rq.body)
        rq.files.posterfile.mv(__dirname+'/'+rq.body.poster,function(err){
          if(err)
            throw 'File Upload Failed'
        })
      }
      update_movie()
    }



    rs.send('OK::'+movie_id)
  }catch(err){
    rs.send('ERROR::'+err.toString())
  }
})

app.post('/search',function(rq,rs){
  let movies = db.prepare(
    `SELECT movies.id,movies.title,year,rating,poster,type FROM movies 
    INNER JOIN movie_info ON movies.id=movie_info.id 
    LEFT JOIN movie_tag ON movies.id=movie_tag.movie_id 
    INNER JOIN tags ON tags.id=movie_tag.tag_id
    WHERE title LIKE @q OR name LIKE @q OR year LIKE @q GROUP BY movies.id ORDER BY title`
  ).all({'q':`%${rq.body.title}%`})

  movies.forEach((movie)=>{
    movie.genres = get_tags.all(movie.id)
    if (Number.isNaN(parseFloat(movie.rating))) movie.rating = 0
  })

  rs.render('movies_list.html',{'movies':movies,'editable':false})
})

app.post('/advancedsearch',function(rq,rs){
  let query = ''
  if( rq.body.genres ){
      let genres
    if(  typeof rq.body.genres == 'string' ){
      genres = rq.body.genres
    }else{
      genres = rq.body.genres.join(',')
    }
    query = `SELECT movies.id,title,year,rating,poster,type,(SELECT COUNT(*) FROM movie_tag WHERE movie_id=movies.id AND tag_id IN (${genres})) as tag_count
    FROM movies INNER JOIN movie_info ON movies.id=movie_info.id WHERE title LIKE @title AND tag_count!=0 AND (@type='0' OR type=@type) ORDER BY tag_count DESC`
  }else{
    query = `SELECT movies.id,title,year,rating,poster,type FROM movies
    INNER JOIN movie_info ON movies.id=movie_info.id WHERE title LIKE @title AND (@type='0' OR type=@type)`
  }
  let movies = db.prepare( query ).all({'title':`%${rq.body.title}%`,'type':rq.body.type})
  movies.forEach((movie)=>{
    movie.genres = get_tags.all(movie.id)
    if (Number.isNaN(parseFloat(movie.rating))) movie.rating = 0
  })

  rs.render('movies_list.html',{'movies':movies,'editable':false})
})

app.get('/removemovie',function(rq,rs){
  try{
    let remove_movie = db.transaction(()=>{
    let id = rq.query.id
    let posterfile = db.prepare('SELECT poster FROM movie_info WHERE id=?').pluck(true).get(id)
      db.prepare('DELETE FROM movie_tag WHERE movie_id=?').run(id)
      db.prepare('DELETE FROM movie_info WHERE id=?').run(id)
      db.prepare('DELETE FROM movies WHERE id=?').run(id)
      if( posterfile != ''){
        fs.unlinkSync(__dirname+'/'+posterfile)
      }
    })
    remove_movie()
    rs.send('OK')
  }catch(e){
    rs.send("err::"+e)
  }
})


process.on('exit',() => db.close())
// app.listen(80)
app.listen(8080)









console.log("---Server Started---")




