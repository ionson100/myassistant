package com.example.user.ling.orm2;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class AnotationOrm {
    public static String getTableName(Class aClass) {
        Temp t = new Temp();
        getTableNameInner(aClass, t);
        return t.name;
    }

    public static ItemField getKeyName(Class aClass) {

        ItemField res = null;
        List<Field> df = getAllFields(aClass);
        for (Field f : df) {
            if (f.isAnnotationPresent(PrimaryKey.class)) {
                final PrimaryKey key = f.getAnnotation(PrimaryKey.class);
                res = new ItemField();
                res.type = f.getType();
                res.fieldName = f.getName();
                res.columnName = key.value();
                res.field = f;
                break;
            }
        }
        return res;
    }

    public static List<ItemField> getListColumn(Class aClass) {


        List<ItemField> list = new ArrayList<>();
        for (Field f : getAllFields(aClass)) {
            if (f.isAnnotationPresent(Column.class)) {
                final Column key = f.getAnnotation(Column.class);
                ItemField fi = new ItemField();
                fi.columnName = key.value();
                fi.fieldName = f.getName();
                fi.type = f.getType();
                list.add(fi);
                fi.field = f;
            }
        }
        return list;
    }

    private static List<Field> getAllFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            fields.addAll(getAllFields(superClazz));
        }
        return fields;
    }

    static void getTableNameInner(Class clazz, Temp table) {

        try {
            if (clazz.isAnnotationPresent(Table.class)) {
                table.name = ((Table) clazz.getAnnotation(Table.class)).value();
            } else {
                Class superClazz = clazz.getSuperclass();
                getTableNameInner(superClazz, table);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    static class Temp {
        public String name;

//        public Temp() {
//            this.name = s;
//        }
    }

}
