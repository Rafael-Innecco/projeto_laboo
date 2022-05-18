# Super Gerenciador Musical

<img src="imagens/logo_spotify.jpg" alt="logo do spotify">

> Projeto para a disciplina de Laboratório de Programação Orientada à Objetos, oferecimento 2022.

### Ajustes e melhorias

O projeto ainda está em desenvolvimento. Abaixo estão as funcionalidades que já estão implementadas e as que estão por vir:

- [x] O sistema permite que o usuário crie uma ou mais playlists com suas músicas preferidas. Cada playlist tem um nome definido pelo usuário. O sistema permite a criação, remoção e listagem das playlists existentes. Ademais, também é possível inserir e remover músicas de uma playlist.
- [ ] O sistema permite a busca por músicas de acordo com vários critérios: título, autor, nome do álbum, nome de playlists públicas do spotify.
- [ ] Para cada resultado de uma busca, o sistema permite uma visão detalhada das músicas oferecendo uma série de parâmetros sobre a música, incluindo dançável, energia, andamento (tempo), força (loudness), fala (speechiness), instrumental, ao vivo, acústica.
- [ ] Ao exibir um álbum ou playlist, o sistema mostra as imagens a ele associadas. Ao exibir uma playlist, o sistema mostra todas as informações disponíveis sobre cada um das músicas (título, artistas, ano, duração).
- [ ] O sistema permite ao usuário filtrar as buscas por músicas em uma determinada tonalidade, determinado modo (maior ou menor) e determinada fórmula de compasso (3/4, 4/4, 5/4, 6/8).
- [ ] O sistema permite a busca dentro de todas as playlists que ele criou usando um critério baseado nos parâmetros de análise do áudio das músicas (dançável, acústica, energia, etc.).

## 💻 Pré-requisitos

Antes de começar, verifique se você atendeu aos seguintes requisitos:
* Você instalou a versão mais recente de [Spring Boot](https://spring.io/projects/spring-boot);
    * Na [Eclipse IDE](https://www.eclipse.org/ide/), é possível obter o Spring Boot e mais ferramentas através do plugin [Spring Tools](https://marketplace.eclipse.org/content/spring-tools-4-aka-spring-tool-suite-4);
* Você leu [como instalar o Super Gerenciador Musical](#instalando-o-super-gerenciador-musical) e [como usar o Super Gerenciador Musical](#usando-o-super-gerenciador-musical).

## 🚀 Instalando o Super Gerenciador Musical

Para instalar o Super Gerenciador Musical, siga estas etapas:

- Clone o repositório;
- Abra o respositório como um novo projeto na sua IDE (na Eclipse IDE: File -> Open Projects from File System);
- Atualize as dependências Maven (na Eclipse IDE: Project -> Update Maven Project);

## ☕ Usando o Super Gerenciador Musical

Para utilizar o projeto, siga essas etapas:

- Rode o aplicativo como um _aplicativo Spring Boot_ (na Eclipse IDE: Run -> Run as -> Spring Boot App)
- Abra o endereço [localhost, na porta 8080](http://localhost:8080)

<b>IMPORTANTE</b>

Nosso projeto está registrado com status [_Development Mode_ no programa Spotify for Developers](https://developer.spotify.com/documentation/web-api/guides/development-extended-quota-modes/). Como consequência, somente usuários autorizados manualmente pelos mantedores do projeto têm permissão de usar o Super Gerenciador Musical.

Por esse motivo, um usuário teste foi criado para que qualquer pessoa possa testar o projeto. Ao carregar a página inicial do Super Gerenciador Musical e clicar em "Autorizar", as seguintes credenciais podem ser utilizadas no login:

Email: labpoo.test.user@gmail.com <br> Senha: LabPOO#2022

Após o login, as funcionalidades implementadas até então se encontrarão na página.
[Clique aqui para descobrir como utilizá-las](./docs/ComoUsar.md)

[⬆ Voltar ao topo](#super-gerenciador-musical)<br>
