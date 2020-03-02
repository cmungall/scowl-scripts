@main
def main(f: String, o: String): Unit = {
  val ontology = loadOntology(f)
  val filteredAxioms = ontology.getAxioms().asScala.collect{ case ax@SubClassOf(_, _, _) => ax}.toSet
  saveNewOntology(ontology, filteredAxioms, o)
}
