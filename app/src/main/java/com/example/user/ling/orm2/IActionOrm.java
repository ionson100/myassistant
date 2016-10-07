package com.example.user.ling.orm2;


interface IActionOrm<T> {
    void actionBeforeUpdate(T t);

    void actionAfterUpdate(T t);

    void actionBeforeInsert(T t);

    void actionAfterInsert(T t);

    void actionBeforeDelete(T t);

    void actionAfterDelete(T t);
}
