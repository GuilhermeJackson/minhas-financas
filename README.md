# Sistema Financeiro
Este projeto permite que os usuários realizem operações financeiras, como criar, atualizar e excluir lançamentos financeiros (receitas ou despesas), verificar seu saldo e autenticação do usuário. Encontramos também outros recursos para validação de regras de negócios, como evitar registros duplicados de usuários com o mesmo e-mail ou nome entre outros.



## API Reference
### /usuarios
#### Salvar novo usuário

```http
  POST /api/usuarios
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `N/A` | `void` | **Salvar um novo usuário no banco de dados** |

```
RequestBody
{
    "nome": String,
    "email": String
    "senha": String
}
```

#### Autenticar usuário

```http
  POST /api/usuarios/autenticar
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `N/A`      | `void` | **Autenticação do usuário na aplicação** |

```
RequestBody
{
    "email": String
    "senha": String
}
```

#### Buscar saldo do usuário

```http
  GET /api/usuarios/${id}/saldo
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `Integer` | `BigDecimal` | **Retorna o valor do saldo do usuário** |

```
Response - 200
{
    "saldo": BigDecimal
}
```



## Modo de instalar
- Instale Java 17, Spring Boot e PostgreSQL;
- Clone o projeto na sua maquina e importe o projeto para sua IDE;
- Crie um novo banco de dados com o nome de 'minhasfinancas' no PostgreSQL
- Crie um schema em PostgreSQL > Database > minhasfinancas > Schemas > novo schme com nome 'financas'
- Rode a aplicação Java
