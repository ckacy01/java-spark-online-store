package org.technoready.dao;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.technoready.entity.Item;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Item.class)
public interface ItemDao {

    @SqlUpdate("INSERT INTO Items (name, description, price)" +
            "VALUES (:name, :description, :price)")
    @GetGeneratedKeys
    long insert(@BindBean Item Item);

    @SqlQuery("SELECT * FROM Items")
    List<Item> findAll();

    @SqlQuery("SELECT * FROM Items WHERE name = :name")
    Optional<Item> findByName(@Bind("name") String name);

    @SqlQuery("SELECT * FROM Items WHERE id = :id")
    Optional<Item> findById(@Bind("id") Long id);

    @SqlUpdate("UPDATE Items SET name = :name, description = :description, price = :price " +
            "WHERE id = :id")
    int update(@BindBean Item Item);

    @SqlUpdate("DELETE FROM Items WHERE id = :id")
    int delete(@Bind("id") Long id);

    @SqlQuery("SELECT EXISTS(SELECT 1 FROM Items WHERE id = :id)")
    boolean exists(@Bind("id") Long id);

}


