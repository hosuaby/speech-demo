package adelean.speech;


import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.stream.Stream;

public class DemoSpeech {
  static LiveSpeechRecognizer recognizer;

  static final RestHighLevelClient elasticClient = new RestHighLevelClient(
      RestClient.builder(
          new HttpHost("localhost", 9200, "http")));

  public static void main(String[] args) throws IOException {
    recognizer = new LiveSpeechRecognizer(config());
    speechLoop();
  }

  static void speechLoop() throws IOException {
    recognizer.startRecognition(true);

    while (true) {
      SpeechResult result = recognizer.getResult();
      String hypothesis = result.getHypothesis();

      System.out.println("HYPOTHESIS: " + hypothesis);
      testHypothesis(hypothesis);
    }
  }

  static Configuration config() {
    Configuration configuration = new Configuration();
    configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
    configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
    configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
    return configuration;
  }

  static void testHypothesis(String hypothesis) throws IOException {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchQuery("text", hypothesis));

    SearchRequest searchRequest = new SearchRequest("alice");
    searchRequest.source(searchSourceBuilder);

    SearchResponse searchResponse = elasticClient
        .search(searchRequest, RequestOptions.DEFAULT);
    Stream.of(searchResponse.getHits().getHits())
        .map(SearchHit::getSourceAsMap)
        .map(source -> source.get("text"))
        .forEach(System.out::println);
  }
}
