# java-filmorate
Template repository for Filmorate project.

### ER-диаграмма базы данных

---

![Filmorate diagram](https://github.com/BiryulevaIrina/java-filmorate/assets/120193162/af88f00d-e6be-4660-aa5e-6b416b5fdcc0)


### Примеры запросов 

---

<details>
  <summary>Получить топ 10 самых популярных фильмов</summary>

```sql
    SELECT f.name,
           COUNT(l.id_user) as likes
    FROM films f
    JOIN likes l ON f.id_film = l.id_film
    ORDER BY COUNT(l.id_user) DESC
    LIMIT 10;
```

</details>  

<details>
  <summary>Получить пользователя с id=10</summary>

```sql
    SELECT *
    FROM users
    WHERE user_id = 10;
```

</details>  

