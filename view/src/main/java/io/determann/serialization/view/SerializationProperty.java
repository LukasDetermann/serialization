package io.determann.serialization.view;

import java.util.Optional;
import java.util.function.Function;

public sealed interface SerializationProperty<TYPE, PROPERTY, SELF extends SerializationProperty<TYPE, PROPERTY, SELF>>
      extends Property<TYPE, PROPERTY, SELF>
      permits ReadAndWriteProperty
{
   Function<TYPE, PROPERTY> getGetter();

   Optional<SerializationView<PROPERTY, ?>> getNestedRead();
}
