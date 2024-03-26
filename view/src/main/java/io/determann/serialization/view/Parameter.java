package io.determann.serialization.view;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface Parameter<TYPE, PROPERTY>
{
   Parameter<TYPE, PROPERTY> withSetting(PropertySetting setting);

   int getIndex();

   String getName();

   Class<PROPERTY> getType();

   Function<Object, PROPERTY> getConverter();

   Optional<DeSerializationView<PROPERTY, ?>> getNestedWrite();

   List<PropertySetting> getSettings();
}