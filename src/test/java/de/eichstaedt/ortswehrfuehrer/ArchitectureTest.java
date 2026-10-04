package de.eichstaedt.ortswehrfuehrer;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.jmolecules.archunit.JMoleculesDddRules;
import org.jmolecules.ddd.annotation.ValueObject;

@AnalyzeClasses(packages = "de.eichstaedt.ortswehrfuehrer")
class ArchitectureTest {

  @ArchTest
  public static final ArchRule dddRules = JMoleculesDddRules.all();

  @ArchTest
  public static final ArchRule valueObjectsShouldBeRecords = classes()
      .that().areAnnotatedWith(ValueObject.class)
      .should().beRecords();
}
