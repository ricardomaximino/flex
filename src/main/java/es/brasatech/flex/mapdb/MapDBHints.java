package es.brasatech.flex.mapdb;

import org.mapdb.DBMaker;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

public class MapDBHints implements RuntimeHintsRegistrar {

    public static final String NET_JPOUNTZ_XXHASH_XXHASH_32_JAVA_SAFE = "net.jpountz.xxhash.XXHash32JavaSafe";
    public static final String NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32_JAVA_SAFE_$_FACTORY = "net.jpountz.xxhash.StreamingXXHash32JavaSafe$Factory";
    public static final String NET_JPOUNTZ_XXHASH_XXHASH_64_JAVA_SAFE = "net.jpountz.xxhash.XXHash64JavaSafe";
    public static final String NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64_JAVA_SAFE_$_FACTORY = "net.jpountz.xxhash.StreamingXXHash64JavaSafe$Factory";
    public static final String NET_JPOUNTZ_XXHASH_XXHASH_FACTORY = "net.jpountz.xxhash.XXHashFactory";
    public static final String NET_JPOUNTZ_XXHASH_XXHASH_32 = "net.jpountz.xxhash.XXHash32";
    public static final String NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32 = "net.jpountz.xxhash.StreamingXXHash32";
    public static final String NET_JPOUNTZ_XXHASH_XXHASH_64 = "net.jpountz.xxhash.XXHash64";
    public static final String NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64 = "net.jpountz.xxhash.StreamingXXHash64";

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(DBMaker.class, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(DBMaker.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(DBMaker.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        // If you use custom serializers or POJOs
        hints.reflection().registerType(MapDBData.class, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(MapDBData.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(MapDBData.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        hints.reflection().registerType(MapDBDataStore.class, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(MapDBDataStore.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(MapDBDataStore.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        hints.reflection().registerType(MapDBDataServiceImpl.class, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(MapDBDataServiceImpl.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(MapDBDataServiceImpl.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        hints.reflection().registerType(SearchDataRepositoryImpl.class, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(SearchDataRepositoryImpl.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(SearchDataRepositoryImpl.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        hints.reflection().registerType(MapDBDataServiceImpl.class, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(MapDBDataServiceImpl.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(MapDBDataServiceImpl.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_32_JAVA_SAFE), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_32_JAVA_SAFE), MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_32_JAVA_SAFE), MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_32_JAVA_SAFE), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_32_JAVA_SAFE), MemberCategory.DECLARED_FIELDS);

        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32_JAVA_SAFE_$_FACTORY), MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32_JAVA_SAFE_$_FACTORY), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32_JAVA_SAFE_$_FACTORY), MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32_JAVA_SAFE_$_FACTORY), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32_JAVA_SAFE_$_FACTORY), MemberCategory.DECLARED_FIELDS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32_JAVA_SAFE_$_FACTORY), MemberCategory.PUBLIC_FIELDS);

        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_64_JAVA_SAFE), MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_64_JAVA_SAFE), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_64_JAVA_SAFE), MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_64_JAVA_SAFE), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_64_JAVA_SAFE), MemberCategory.DECLARED_FIELDS);

        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64_JAVA_SAFE_$_FACTORY), MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64_JAVA_SAFE_$_FACTORY), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64_JAVA_SAFE_$_FACTORY), MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64_JAVA_SAFE_$_FACTORY), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64_JAVA_SAFE_$_FACTORY), MemberCategory.DECLARED_FIELDS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64_JAVA_SAFE_$_FACTORY), MemberCategory.PUBLIC_FIELDS);

        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_FACTORY), MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_32), MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32), MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_64), MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64), MemberCategory.INVOKE_PUBLIC_METHODS);

        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_FACTORY), MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_32), MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32), MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_64), MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64), MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);

        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_FACTORY), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_32), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_64), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_FACTORY), MemberCategory.DECLARED_CLASSES);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_32), MemberCategory.DECLARED_CLASSES);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_32), MemberCategory.DECLARED_CLASSES);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_XXHASH_64), MemberCategory.DECLARED_CLASSES);
        hints.reflection().registerType(TypeReference.of(NET_JPOUNTZ_XXHASH_STREAMING_XXHASH_64), MemberCategory.DECLARED_CLASSES);

    }
}