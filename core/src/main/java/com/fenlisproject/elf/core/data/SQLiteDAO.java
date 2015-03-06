package com.fenlisproject.elf.core.data;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;

import com.fenlisproject.elf.core.annotation.Column;
import com.fenlisproject.elf.core.annotation.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDAO {

    private final int FIELD_TYPE_NULL = 0;
    private final int FIELD_TYPE_INTEGER = 1;
    private final int FIELD_TYPE_FLOAT = 2;
    private final int FIELD_TYPE_STRING = 3;
    private final int FIELD_TYPE_BLOB = 4;
    private final SQLiteDatabase db;

    public SQLiteDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public <T> List<T> query(Class<T> classOfT, String[] columns, String selection,
                             String[] selectionArgs, String groupBy, String having,
                             String orderBy, String limit) {
        List<T> data = new ArrayList<>();
        Annotation classAnnotation = classOfT.getAnnotation(Table.class);
        if (classAnnotation instanceof Table) {
            String tableName = ((Table) classAnnotation).name();
            Cursor cursor = db.query(tableName, columns, selection, selectionArgs, groupBy,
                    having, orderBy, limit);
            data = fetchCursor(classOfT, cursor);
        }
        return data;
    }

    public <T> List<T> queryAll(Class<T> classOfT) {
        return query(classOfT, null, null, null, null, null, null, null);
    }

    public <T> List<T> rawQuery(Class<T> classOfT, String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return fetchCursor(classOfT, cursor);
    }

    public <T> long insert(T data) {
        Field[] fields = data.getClass().getDeclaredFields();
        Annotation classAnnotation = data.getClass().getAnnotation(Table.class);
        if (classAnnotation instanceof Table) {
            String tableName = ((Table) classAnnotation).name();
            ContentValues values = new ContentValues();
            for (Field field : fields) {
                field.setAccessible(true);
                Annotation fieldAnnotation = field.getAnnotation(Column.class);
                if (fieldAnnotation instanceof Column) {
                    try {
                        Object value = field.get(data);
                        if (value instanceof Integer) {
                            values.put(((Column) fieldAnnotation).name(), (int) value);
                        } else if (value instanceof Float) {
                            values.put(((Column) fieldAnnotation).name(), (float) value);
                        } else if (value instanceof Double) {
                            values.put(((Column) fieldAnnotation).name(), (double) value);
                        } else if (value instanceof Boolean) {
                            values.put(((Column) fieldAnnotation).name(), (boolean) value);
                        } else if (value != null) {
                            values.put(((Column) fieldAnnotation).name(), String.valueOf(value));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return db.insert(tableName, null, values);
        }
        return -1;
    }

    public <T> long update(T data, String filter) {
        Field[] fields = data.getClass().getDeclaredFields();
        Annotation classAnnotation = data.getClass().getAnnotation(Table.class);
        if (classAnnotation instanceof Table) {
            String tableName = ((Table) classAnnotation).name();
            ContentValues values = new ContentValues();
            for (Field field : fields) {
                field.setAccessible(true);
                Annotation fieldAnnotation = field.getAnnotation(Column.class);
                if (fieldAnnotation instanceof Column) {
                    try {
                        Object value = field.get(data);
                        if (value instanceof Integer) {
                            values.put(((Column) fieldAnnotation).name(), (int) value);
                        } else if (value instanceof Float) {
                            values.put(((Column) fieldAnnotation).name(), (float) value);
                        } else if (value instanceof Double) {
                            values.put(((Column) fieldAnnotation).name(), (double) value);
                        } else if (value instanceof Boolean) {
                            values.put(((Column) fieldAnnotation).name(), (boolean) value);
                        } else if (value != null) {
                            values.put(((Column) fieldAnnotation).name(), String.valueOf(value));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return db.update(tableName, values, filter, null);
        }
        return -1;
    }

    private <T> List<T> fetchCursor(Class<T> classOfT, Cursor cursor) {
        List<T> data = new ArrayList<>();
        Field[] fields = classOfT.getDeclaredFields();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                try {
                    T item = classOfT.newInstance();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Annotation annotation = field.getAnnotation(Column.class);
                        if (annotation instanceof Column) {
                            int columnIndex = cursor.getColumnIndex(
                                    ((Column) annotation).name());
                            if (columnIndex != -1) {
                                int columnType = getType(cursor, columnIndex);
                                try {
                                    switch (columnType) {
                                        case Cursor.FIELD_TYPE_NULL:
                                        case Cursor.FIELD_TYPE_BLOB:
                                            break;
                                        case Cursor.FIELD_TYPE_INTEGER:
                                            field.set(item, cursor.getInt(columnIndex));
                                            break;
                                        case Cursor.FIELD_TYPE_FLOAT:
                                            field.set(item, cursor.getFloat(columnIndex));
                                            break;
                                        case Cursor.FIELD_TYPE_STRING:
                                            field.set(item, cursor.getString(columnIndex));
                                            break;
                                    }
                                } catch (IllegalAccessException iacce) {
                                    iacce.printStackTrace();
                                } catch (IllegalArgumentException iarge) {
                                    iarge.printStackTrace();
                                }
                            }
                        }
                    }
                    data.add(item);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
        return data;
    }

    public boolean delete(String tableName, String filter, String[] filterArgs) {
        int affected_rows = db.delete(tableName, filter, filterArgs);
        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public void closeDatabase() {
        db.close();
    }

    private int getType(Cursor cursor, int columnIndex) {
        SQLiteCursor sqLiteCursor = (SQLiteCursor) cursor;
        CursorWindow cursorWindow = sqLiteCursor.getWindow();
        int pos = cursor.getPosition();
        int type = -1;
        if (cursorWindow.isNull(pos, columnIndex)) {
            type = FIELD_TYPE_NULL;
        } else if (cursorWindow.isLong(pos, columnIndex)) {
            type = FIELD_TYPE_INTEGER;
        } else if (cursorWindow.isFloat(pos, columnIndex)) {
            type = FIELD_TYPE_FLOAT;
        } else if (cursorWindow.isString(pos, columnIndex)) {
            type = FIELD_TYPE_STRING;
        } else if (cursorWindow.isBlob(pos, columnIndex)) {
            type = FIELD_TYPE_BLOB;
        }
        return type;
    }
}
