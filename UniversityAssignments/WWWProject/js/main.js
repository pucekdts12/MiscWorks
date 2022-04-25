'use strict';
  
function enlargeImage(elem){
  let img = document.createElement('img')
  img.className="posterLarge"
  img.src=""+elem.src
  showOverlay( img )
}

function showOverlay(elem,persistent){
  let o = document.querySelector('#overlay')
  o.classList.remove("hidden")
  o.append(elem)
  o.persistent = persistent | false
  
}

function closeOverlay(force){
  let over = document.querySelector('#overlay')
  if( over.persistent && !force ) return;
  over.classList.add("hidden")
  over.innerHTML=''
}

function openAdvancedSearch(){
  // showOverlay(,true)
  ajax({
    url:"/advancedSearch",
    success:function(xhr){
      let elem = document.createElement('div')
      elem.innerHTML=xhr.response
      showOverlay( elem.childNodes[0],true )
    }
  })
}

function advancedSearch(form){
  let data = new FormData(form)
  let xhr = new XMLHttpRequest()
  xhr.onload=function(){
    // window.location.replace("/")
    // window.history.pushState("advsearch", "", "/");
    window.history.replaceState("advsearch", "", "/");
    document.querySelector('#movies').innerHTML=this.responseText
    closeOverlay(true)
  }
  xhr.open('POST','/advancedSearch',true)
  xhr.send(data)
}

function simpleSearch(form){
  let data = {}
  new FormData(form).forEach((value,key)=>data[key] = value)
  console.log(data)
  ajax({
    method:'post',
    url:"/search",
    data:data,
    success:(xhr)=>{
      // window.location.replace("/")
      // window.history.pushState("simsearch", "", "/");
      window.history.replaceState("simsearch", "", "/");
      document.querySelector('#movies').innerHTML=xhr.response
    }
  })
}

function previewPoster(src,target){
  if( src.files && src.files[0] ){
    target = document.querySelector(target)
    let fr = new FileReader()
    fr.onload=(e)=>{
      target.src = e.target.result
    }
    fr.readAsDataURL(src.files[0])
  }
}

window.onload=function(){
  
  
  // document.querySelectorAll(".poster").onclick=enlargeImage;

  let movies = document.querySelector('#movies')
  if( movies ){
    movies.addEventListener('click',(e)=>{
      let elem = e.target
      if( elem.classList.contains("poster") ){
        enlargeImage(elem)
        e.stopPropagation()
      }else if( elem.tagName=="A" ){
        return;
      }else if( elem.closest('.movie')!=null ){
        elem.closest('.movie').classList.toggle('alwaysShow')
        e.stopPropagation()
      }
    })
  }

  

  document.querySelector("#overlay").onclick=function(e){
    closeOverlay();
    e.stopPropagation()
  };


  window.addEventListener("keydown",function(e){
    if( e.key=='Escape' ){
      closeOverlay()
      e.stopPropagation()
    }
  });

  //openAdvancedSearch(); // for testing
}