package org.crashtest.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.*;
import net.sf.extcos.ComponentQuery;
import net.sf.extcos.ComponentScanner;
import net.sf.extcos.internal.JavaClassResourceType;
import org.crashtest.annotations.Entry;
import org.crashtest.annotations.Fixture;
import org.crashtest.request.EntryPointInvocation;
import org.crashtest.request.ParameterInvocation;
import org.crashtest.response.EntryPoint;
import org.crashtest.response.Parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClasspathScanningEntryPointService implements EntryPointService {
    private final Map<String, EntryPoint> nameToEntryPoint;
    private final Map<EntryPoint, Method> entryPointToMethod;
    LoadingCache<Method, Object> methodToInstance;

    private ClasspathScanningEntryPointService() {
        ImmutableMap.Builder<String, EntryPoint> builder = ImmutableMap.builder();
        ImmutableMap.Builder<EntryPoint, Method> entryToMethod = ImmutableMap.builder();
        Set<Class<?>> classes = getFixtureClasses();
        for (Class<?> clazz : classes) {
            Map<Method, EntryPoint> entryPoints = getAllEntryPoints(clazz);
            builder.putAll(Maps.uniqueIndex(entryPoints.values(), EntryPoint.GET_NAME));
            entryToMethod.putAll(ImmutableBiMap.copyOf(entryPoints).inverse());
        }
        nameToEntryPoint = builder.build();
        entryPointToMethod = entryToMethod.build();
        methodToInstance = CacheBuilder.newBuilder().build(CacheLoader.from(new Function<Method, Object>() {
            @Override
            public Object apply(Method input) {
                try {
                    return input.getDeclaringClass().newInstance();
                } catch (Exception e) {
                    return null;
                }
            }
        }));
    }

    private Map<Method, EntryPoint> getAllEntryPoints(Class<?> clazz) {
        ImmutableMap.Builder<Method, EntryPoint> entryPoints = ImmutableMap.builder();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Entry.class) && allParametersAreAnnotated(method)) {
                Entry annotation = method.getAnnotation(Entry.class);
                EntryPoint.Builder entry = EntryPoint.named(annotation.value() == null ? method.getName() : annotation.value());
                for (Annotation[] annotations : method.getParameterAnnotations()) {
                    Annotation annotation1 = Iterables.find(ImmutableList.copyOf(annotations), new Predicate<Annotation>() {
                        @Override
                        public boolean apply(Annotation input) {
                            return input.annotationType().equals(Entry.class);
                        }
                    });
                    entry.withParameter(Parameter.named(((Entry) annotation1).value()));
                }
                entryPoints.put(method, entry.build());
            }
        }
        return entryPoints.build();
    }

    private boolean allParametersAreAnnotated(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        boolean allParametersAnnotated = true;
        for (int i = 0; i < parameterTypes.length; i++) {
            List<Class<?>> annotationTypes = Lists.transform(ImmutableList.copyOf(parameterAnnotations[i]), new Function<Annotation, Class<?>>() {
                @Override
                public Class<?> apply(Annotation input) {
                    return input.annotationType();
                }
            });
            allParametersAnnotated = annotationTypes.contains(Entry.class);
        }
        return allParametersAnnotated;
    }

    private Set<Class<?>> getFixtureClasses() {
        ComponentScanner scanner = new ComponentScanner();
        return scanner.getClasses(new ComponentQuery() {
            protected void query() {
                select(JavaClassResourceType.javaClasses()).from("org.crashtest").returning(allAnnotatedWith(Fixture.class));
            }
        });
    }

    @Override
    public Collection<EntryPoint> getAllEntryPoints() {
        return nameToEntryPoint.values();
    }

    @Override
    public void invoke(EntryPointInvocation entryPointInvocation) throws NonExistentEntryPointException, EntryPointInvocationException {
        EntryPoint entryPoint = nameToEntryPoint.get(entryPointInvocation.getEntryPointName());
        if (entryPoint == null) {
            throw new NonExistentEntryPointException(entryPointInvocation.getEntryPointName());
        } else {
            List<String> invocationParams = Lists.transform(entryPointInvocation.getParameters(), ParameterInvocation.GET_NAME);
            List<String> entryParams = entryPoint.getParameterNames();
            if (invocationParams.equals(entryParams)) {
                Method method = entryPointToMethod.get(entryPoint);
                try {
                    method.invoke(methodToInstance.get(method), Lists.transform(entryPointInvocation.getParameters(), ParameterInvocation.GET_VALUE).toArray());
                } catch (Exception e) {
                    throw new EntryPointInvocationException(e);
                }
            } else {
                throw new EntryPointInvocationException("parameters did not match");
            }
        }
    }

    public static EntryPointService newInstance() {
        return new ClasspathScanningEntryPointService();
    }
}
