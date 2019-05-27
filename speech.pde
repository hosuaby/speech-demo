import edu.cmu.sphinx.*;
import processing.sound.*;

LiveSpeechRecognizer recognizer;

void setup() {
  size(200,200);
  background(0);

  try {
    recognizer = new LiveSpeechRecognizer(config());
    recognizer.startRecognition(true);
  } catch (IOException ioException) {
    println(ioException);
  }
}
  
void draw() {
  SpeechResult result = recognizer.getResult();
  
  //List<WordResult> = result.getWords();
  
  //Lattice lattice = result.getLattice();
  //lattice.dumpAllPaths();
  
  String hypothesis = result.getHypothesis();
  println("HUPOTHESIS: " + hypothesis);
}

Configuration config() {
  Configuration configuration = new Configuration();
  configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
  configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
  configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
  return configuration;
}