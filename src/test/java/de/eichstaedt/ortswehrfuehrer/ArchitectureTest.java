package de.eichstaedt.ortswehrfuehrer;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.jmolecules.archunit.JMoleculesDddRules;

@AnalyzeClasses(packages = "de.eichstaedt.ortswehrfuehrer")
class ArchitectureTest {

  @ArchTest
  ArchRule dddRules = JMoleculesDddRules.all();
}
