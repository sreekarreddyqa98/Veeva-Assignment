package org.veeva.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RunnerGenerator {

    private static final String FEATURES_DIR = "src/test/resources/features";
    private static final String RUNNERS_DIR = "src/test/java/org/veeva/runner";

    public static void main(String[] args) throws IOException {
        File featureDir = new File(FEATURES_DIR);
        File[] featureFiles = featureDir.listFiles((dir, name) -> name.endsWith(".feature"));

        if (featureFiles == null) return;

        new File(RUNNERS_DIR).mkdirs();

        for (File feature : featureFiles) {
            String featureName = feature.getName().replace(".feature", "");
            String className = capitalize(featureName) + "Runner";
            String classContent = generateRunnerClass(className, feature.getName());

            File javaFile = new File(RUNNERS_DIR + "/" + className + ".java");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile))) {
                writer.write(classContent);
            }
        }
    }

    private static String generateRunnerClass(String className, String featureFileName) {
        return "package org.veeva.runner;\n\n" +
                "import io.cucumber.testng.AbstractTestNGCucumberTests;\n" +
                "import io.cucumber.testng.CucumberOptions;\n\n" +
                "@CucumberOptions(\n" +
                "    features = \"src/test/resources/features/" + featureFileName + "\",\n" +
                "    glue = \"org.veeva.stepDefinitions\",\n" +
                "    plugin = {\"pretty\", \"html:target/" + className + "-report.html\"},\n" +
                "    monochrome = true\n" +
                ")\n" +
                "public class " + className + " extends AbstractTestNGCucumberTests {\n" +
                "}";
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
