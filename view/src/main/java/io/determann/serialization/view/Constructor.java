package io.determann.serialization.view;

public sealed interface Constructor<TYPE> permits Constructor0,
                                                  Constructor1,
                                                  Constructor2
{
}
