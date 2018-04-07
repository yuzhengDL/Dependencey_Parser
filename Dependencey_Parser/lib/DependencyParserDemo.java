
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

import java.io.StringReader;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;

/**
 * Demonstrates how to first use the tagger, then use the NN dependency
 * parser. Note that the parser will not work on untagged text.
 *
 * @author Jon Gauthier
 */
public class DependencyParserDemo {
  public static void main(String[] args) {
    String modelPath = DependencyParser.DEFAULT_MODEL;
    String taggerPath = "stanford-tagger/models/english-left3words-distsim.tagger";

    for (int argIndex = 0; argIndex < args.length; ) {
      switch (args[argIndex]) {
        case "-tagger":
          taggerPath = args[argIndex + 1];
          argIndex += 2;
          break;
        case "-model":
          modelPath = args[argIndex + 1];
          argIndex += 2;
          break;
        default:
          throw new RuntimeException("Unknown argument " + args[argIndex]);
      }
    }

    String text = "Black dog chasing a young child.";

    MaxentTagger tagger = new MaxentTagger(taggerPath);
    DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
    for (List<HasWord> sentence : tokenizer) {
      List<TaggedWord> tagged = tagger.tagSentence(sentence);

      int len = tagged.size();
      Collection<TypedDependency> tdl = parser.predict(tagged).typedDependencies();
      //GrammaticalStructure gs = parser.predict(tagged);
      int[] parents = new int[len];
      for (int i = 0; i < len; i++){
        parents[i] = -1;
      }

      String[] relns = new String[len];
      for (TypedDependency td : tdl){
        int child = td.dep().index();
        int parent = td.gov().index();
        relns[child - 1] = td.reln().toString();
        parents[child - 1] = parent;
      }

      // Print typed dependencies
      System.err.println(tdl);
      System.err.println(Arrays.toString(relns));
      System.err.println(Arrays.toString(parents));
    }
  }
}
