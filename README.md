# java-filmorate
Template repository for Filmorate project.

### ER-диаграмма базы данных

---

![Filmorate diagram ](https://github.com/BiryulevaIrina/java-filmorate/assets/120193162/6ef6c8af-94dd-4c99-939d-af41ea26c5ad)


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

