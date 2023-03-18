package main.com.wazteam;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class EntityComponent {

    public static FileOutput filerw() {
        return new FileOutput();
    }

    public static List get(String collectionFileName) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        String content = filerw().output(collectionFileName);
        List data = (List) Serializer.deserialize(content.getBytes());
        return data;
    }

    public static List sortData(List data) {
        List result = new ArrayList();
        if(data != null) {
            for(int i = data.size() - 1; i >= 0; i--) {
                result.add(data.get(i));
            }
        }
        return result;
    }

    public static List find(String collectionFileName) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return get(collectionFileName);
    }

    public static List findDsc(String collectionFileName) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return sortData(get(collectionFileName));
    }

    public static List find(String collectionFileName, int page, int ndata) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        List data = get(collectionFileName);
        List dataFilter = new ArrayList();

        if(data != null) {
            int size = data.size();

            if(size >= page * ndata) {
                for(int i = (page - 1) * ndata; i < page * ndata; i++) {
                    dataFilter.add(data.get(i));
                }
            }

            if(size<page * ndata) {
                if(page == 1) {
                    for(int i = ((page - 1) * ndata); i < size; i++) {
                        dataFilter.add(data.get(i));
                    }
                } else {
                    for(int i = ((page - 1) * ndata); i < page*ndata; i++) {
                        dataFilter.add(data.get(i));
                    }
                }

            }
        }

        return sortData(dataFilter);
    }

    public static List filter(String collectionFileName, String id, int page, int ndata) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        List dataFilter = new ArrayList();
        List data = filter(collectionFileName, id);

        if(data != null) {
            int size = data.size();

            if(size > page * ndata) {
                for(int i = (page - 1) * ndata; i < page * ndata; i++) {
                    dataFilter.add(data.get(i));
                }
            }

            if(size < page * ndata) {
                for(int i = size - 1; i < (page - 1) * ndata; i++) {
                    dataFilter.add(data.get(i));
                }
            }
        }

        return sortData(dataFilter);
    }

    public static List filter(String collectionFileName, String id) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        List filterResult = new ArrayList();
        List data = get(collectionFileName);
        if(data != null) {
            for(Object filterObject : data) {
                addObject(filterObject, filterResult, id);
            }
        }
        return filterResult;
    }

    public static Object findOne(String collectionFileName, String id) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        List data = get(collectionFileName);
        Object result = null;
        if(data != null) {
            for(Object filterObject : data) {
                result = findObject(filterObject, id);
                if(result != null) {
                    return result;
                }
            }
        }
        return result;
    }

    public static void add(String collectionFileName, Object o) {
        try {
            filerw().remove(collectionFileName);
            //filerw().write(collectionFileName, "");
            String filepath = filerw().filepath(collectionFileName);
            Serializer.serialize(collectionFileName, o);

        } catch (IllegalArgumentException | InterruptedException | SecurityException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }


    }

    public static void addObject(Object founded, List result, String predicat) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
        Object obj = findObject(founded, predicat);
        if(obj != null) {
            result.add(obj);
        }
    }

    public static Object findObject(Object founded, String predicat) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
        if(founded != null) {
            for (int i = 0; i < founded.getClass().getDeclaredFields().length; i++) {

                Field f = founded.getClass().getDeclaredFields()[i];

                f.setAccessible(true);
                String fValue = (String) f.get(founded);

                String upper = String.valueOf(f.getName().charAt(0)).toUpperCase();
                String methodName = "get".concat(upper).concat(f.getName().substring(1));
                Method getNameMethod = founded.getClass().getMethod(methodName, new Class[]{});
                f.setAccessible(true);
                String methodValue = getNameMethod.invoke(founded).toString();
                if(methodValue.equals(predicat)) {
                    return founded;
                }
            }
        }
        return null;
    }
}
