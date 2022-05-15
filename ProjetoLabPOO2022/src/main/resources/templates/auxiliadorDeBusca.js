

function busca(){
   console.log("buscando...");
   fetch('/menu/busca-musica?nome-busca=' + document.getElementById("nome-busca-input").value)
      .then(function (response) {
         return response.json();
      })
      .then(function (data) {
         appendData(data);
      })
      .catch(function (err) {
         console.log('error: ' + err);
      });
   function appendData(data) {
      var mainContainer = document.getElementById("resultadosDeBusca");
      for (var i = 0; i < data.length; i++) {
         var h1 = document.createElement("h1");
         h1.innerHTML = 'Name: ' + data[i].name + ' ;';
         console.log('Name: ' + data[i].name + ' ;');
         mainContainer.appendChild(h1);
      }
   }
}