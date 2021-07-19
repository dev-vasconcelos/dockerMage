# docker Mage API
Projeto parado, esperando tempo para refatoração.

Uma API para o gerenciamento da tecnologia docker. Gerenciamento complexo de containers, imagens, dockerfiles e docker-composes, abrindo um ambiente de testes amigável ao usuário.

# path: /containers

POST / : Rodar container || Alterar parâmetro de um container
POST /com : Executar comando no container
POST /cp : Copiar arquivos de um container p/ outro
POST /cp/h : Copiar arquivo do host para container
POST /cp/c : Copiar arquivo do container p/ host
POST /up : Realizar upload de um container rodando p/ registry
POST /stop/{idContainer} : Parar container
POST /start/{idContainer} : Inicializar container
POST /export/{idContainer} : Exportar container para image

DELETE /{idContainer} : Excluir container

GET /{param} : Listar containers
GET /down/{idContainer} : Baixar container .tar.gz

# path: /images

POST / : realizar upload de .tar.gz
POST /up : Realizar upload para registry

DELETE /{idImage} : Deletar Imagem

GET /registry/{param} : Listar Images do registry
GET /local/{param} : Listar imagens servidor local
GET /registry/down : Baixar imagem .tar.gz
GET /local/down : Baixar imagem .tar.gz 

# path : /dockerfile

POST / : Criar dockerfile
POST/build/{idDockerfile} : Buildar dockerfile
POST /up : upload de dockerfile

DELETE /{idDockerfile} : Deletar dockerfile

GET /{param} : Listar dockerfile

# path : /compose

POST / : criar docker-compose
POST /run : Rodar compose
POST /up : Upload de arquivo compose

GET /{param} : Listar compose

DELETE /{idCompose} : Deletar compose
