package io.determann.serialization.view;

public sealed interface SerializationView<TYPE, SELF extends SerializationView<TYPE, SELF>> extends View<TYPE, SELF>
      permits PropertySerializationView,
              PropertyView
{
}
