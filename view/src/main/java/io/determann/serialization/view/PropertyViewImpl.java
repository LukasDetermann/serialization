package io.determann.serialization.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class PropertyViewImpl<TYPE>
{
   private final Supplier<TYPE> instanceSupplier;
   private final List<Property<TYPE, Object, ?>> properties = new ArrayList<>();
   private final List<TypeSetting> settings = new ArrayList<>();

   PropertyViewImpl()
   {
      this.instanceSupplier = null;
   }

   PropertyViewImpl(Supplier<TYPE> instanceSupplier)
   {
      this.instanceSupplier = requireNonNull(instanceSupplier);
   }

   private PropertyViewImpl(PropertyViewImpl<TYPE> view)
   {
      requireNonNull(view);
      this.instanceSupplier = view.getInstanceSupplier();

      String duplicateNames = view.getProperties()
                                  .stream()
                                  .collect(groupingBy(Property::getName))
                                  .entrySet()
                                  .stream()
                                  .filter(entry -> entry.getValue().size() > 1)
                                  .map(Map.Entry::getKey)
                                  .collect(joining(", "));
      if (!duplicateNames.isEmpty())
      {
         throw new IllegalArgumentException("Duplicate names: " + duplicateNames);
      }

      view.getProperties()
          .stream()
          .map(PropertyImpl::new)
          .forEach(this.properties::add);

      if (view.getSettings().stream().map(Setting::getName).anyMatch(String::isBlank))
      {
         throw new IllegalArgumentException("Black Strings are not valid keys");
      }

      this.settings.addAll(view.getSettings());
   }

   public <PROPERTY> PropertyViewImpl<TYPE> with(Property<TYPE, PROPERTY, ?> property)
   {
      PropertyViewImpl<TYPE> newView = new PropertyViewImpl<>(this);
      newView.getProperties().removeIf(p -> p.getName().equals(property.getName()));
      //noinspection unchecked
      newView.getProperties().add((Property<TYPE, Object, ?>) new PropertyImpl<>(property));
      return newView;
   }

   public <PROPERTY> PropertyViewImpl<TYPE> with(Property<TYPE, PROPERTY, ?> property,
                                                 PropertySetting propertySetting)
   {
      requireNonNull(property);
      requireNonNull(propertySetting);

      PropertyImpl<TYPE, PROPERTY, ?> newProperty = new PropertyImpl<>(property);
      PropertyViewImpl<TYPE> copy = new PropertyViewImpl<>(this);
      copy.getProperties().removeIf(p -> p.getName().equals(property.getName()));
      newProperty.getSettings().add(propertySetting);
      //noinspection unchecked
      copy.getProperties().add((Property<TYPE, Object, ?>) newProperty);
      return copy;
   }

   public PropertyViewImpl<TYPE> with(TypeSetting setting)
   {
      PropertyViewImpl<TYPE> newView = new PropertyViewImpl<>(this);
      newView.getSettings().add(setting);
      return newView;
   }

   List<Property<TYPE, Object, ?>> getProperties()
   {
      return properties;
   }

   Supplier<TYPE> getInstanceSupplier()
   {
      return instanceSupplier;
   }

   List<TypeSetting> getSettings()
   {
      return settings;
   }
}
