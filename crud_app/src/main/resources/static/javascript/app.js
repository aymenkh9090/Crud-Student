const deleteButtons = document.querySelectorAll('.bdelete');

deleteButtons.forEach(btn=>{
    btn.addEventListener('click',function(event){
        const confirmDelete =  confirm('Are you sure to delete this student');
        if(!confirmDelete){
            event.preventDefault();
        }
    });
});

setTimeout(()=>{
    const alert = document.querySelector('.al');
    if(alert){
        alert.style.display = 'none';
    }
},5000)