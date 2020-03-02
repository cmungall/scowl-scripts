// put this in ~/.ammonite/predef.sc

import $ivy.`net.sourceforge.owlapi:owlapi-distribution:4.5.6`
import $ivy.`org.phenoscape::scowl:1.3.4`
import $ivy.`org.semanticweb.elk:elk-owlapi:0.4.3`
import $ivy.`com.outr::scribe-slf4j:2.7.11`

import org.semanticweb.owlapi.model._
import org.semanticweb.owlapi.apibinding.OWLManager
import org.phenoscape.scowl._
import java.io.File
import scala.collection.JavaConverters._

def newManager(): OWLOntologyManager = { OWLManager.createOWLOntologyManager() }
var manager = OWLManager.createOWLOntologyManager()
var ontology = manager.createOntology()
def loadOntology(filename: String): OWLOntology = manager.loadOntologyFromOntologyDocument(new File(filename))
def saveOntology(ontology: OWLOntology, filename: String) = manager.saveOntology(ontology, IRI.create(new File(filename)))

def axiomsFromFile(filename: String): Set[OWLAxiom] =   loadOntology(filename).getAxioms().asScala.toSet


// warning: mutates ontology
def saveNewOntology(ontology: OWLOntology, newAxioms: Set[_ <: OWLAxiom], f: String) = {
  manager.removeAxioms(ontology, ontology.getAxioms())
  manager.addAxioms(ontology, newAxioms.asJava)
  saveOntology(ontology, f)
}

def ontFilter(filename: String, ff: OWLAxiom => Boolean) : OWLOntology = {
  val m = newManager()
  val ontology = m.loadOntologyFromOntologyDocument(new File(filename))
  val axioms = ontology.getAxioms().asScala.toSet
  val filteredAxioms = axioms.filter(ff)
  for {ax <- filteredAxioms} { println(ax) }
  manager.removeAxioms(ontology, axioms.asJava)
  manager.addAxioms(ontology, filteredAxioms.asJava)
  return ontology
}

// e.g ontFilter("pato.owl", a => { a match { case a @ SubClassOf(_,_,_) => true; case _ => false}} )
// from CLI:
// amm -p predef.sc -c 'ontFilter("pato.owl", a => { a match { case a @ SubClassOf(_,_,_) => true; case _ => false}} )'
