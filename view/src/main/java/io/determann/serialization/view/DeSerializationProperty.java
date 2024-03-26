package io.determann.serialization.view;

import java.util.Optional;
import java.util.function.BiConsumer;

public sealed interface DeSerializationProperty<TYPE, PROPERTY, SELF extends DeSerializationProperty<TYPE, PROPERTY, SELF>>
      extends Property<TYPE, PROPERTY, SELF> permits ReadAndWriteProperty
{
   BiConsumer<TYPE, PROPERTY> getSetter();

   Optional<DeSerializationView<PROPERTY, ?>> getNestedWrite();
}
