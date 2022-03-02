# api-client

Uma api java no padrão REST e MVC, e realiza um CRUD que disponibiliza dados de clientes.

## Tecnologias Utilizadas
- Java com Spring

Porta padrão do Spring: ``localhost:8080``

## Exemplos Requisições

- GET: ``localhost:8080/clients?page=0&linesPerPage=6&direction=ASC&orderBy=id``
- GET: ``localhost:8080/clients/1``
- POST: ``localhost:8080/clients``
```json
{
    "name": "Rodrigues",
    "cpf": "123.836.759-10",
    "income": 2130.0,
    "birthDate": "2021-09-22T00:00:00Z",
    "children": 2
}
```
- PUT: ``localhost:8080/clients/1``
```json
{
  "name": "Maria Silva",
  "cpf": "12345678901",
  "income": 6500.0,
  "birthDate": "1994-07-20T10:30:00Z",
  "children": 2
}
```
- DELETE: ``localhost:8080/clients/2``

## Como Testar as Requisições?
Sugiro o [Postman](https://www.postman.com)
### Como Executar?
```bash
git clone https://github.com/SamLucena/api-client.git
```
## Teste a API
CI/CD no Heroku: [API-CLIENT](https://clients-samlucena.herokuapp.com)

### Docker
Rode a API em um container do Docker.

- Instale o Docker: [GET-DOCKER](https://docs.docker.com/get-docker/)
```bash
    docker run -p 80:8080 --name dscatalog-docker mydockerid103107/dsclient:v1
```
Por padrão o container do Docker rodará na porta ``8080``, porém ela pode ser mudada na opção ``-p`` do comando Docker.
Neste caso coloquei a porta `80`.
