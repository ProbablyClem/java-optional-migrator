package org.openrewrite.starter;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class NoGuavaListsNewArrayListTest implements RewriteTest {

    //Note, you can define defaults for the RecipeSpec and these defaults will be used for all tests.
    //In this case, the recipe and the parser are common. See below, on how the defaults can be overriden
    //per test.
    @Override
    public void defaults(RecipeSpec spec) {
        spec
          .recipe(new NoGuavaListsNewArrayList())
          .parser(JavaParser.fromJavaVersion()
            .logCompilationWarningsAndErrors(true)
            .classpath("guava")
            .build());
    }

    @Test
    void replaceWithNewArrayList() {
        rewriteRun(
          //There is an overloaded version or rewriteRun that allows the RecipeSpec to be customized specifically
          //for a given test. In this case, the parser for this test is configured to not log compilation warnings.
          spec -> spec
            .parser(JavaParser.fromJavaVersion()
              .logCompilationWarningsAndErrors(false)
              .classpath("guava")
              .build()),
          java(
            """
                  import java.util.Optional;
                  
                  class Test {
                      Optional<String> getOptionalString(){
                        return Optional.ofNullable(null);
                      }
                      
                      int getId(){
                        return 0;
                      }
                      
                      Test(){
                      String val = getOptionalString();
                      int id = getId();
                      }
                  }
              """,
            """
                  import java.util.Optional;
                  
                  class Test {
                      Optional<String> getOptionalString(){
                        return Optional.ofNullable(null);
                      }
                      
                      int getId(){
                        return 0;
                      }
                      
                      Test(){
                      String val = getOptionalString().orElse(null);
                      int id = getId();
                      }
                  }
              """
          )
        );
    }
}
