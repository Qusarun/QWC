package com.qusarun.wordcards;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public enum QWC {
    INSTANCE;

    @Getter private final Map<String, List<String>> languages     = new HashMap<>();
    @Getter private final List<String>              languageNames = new ArrayList<>();
    @Getter private       String                    language;
    @Getter private       GUI                       gui;

    public void load() {
        final String[] languages = new File("languages/").list();
        if (languages == null || languages.length == 0) {
            System.out.println("no languages to load");
            System.exit(1);
        }

        languageNames.addAll(Arrays.asList(languages));
        language = languages[0];
        for (final String language: languages) {
            final List<String> words = new ArrayList<>();
            try {
                final BufferedReader br = new BufferedReader(new FileReader("languages/" + language + "/words"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains(":"))
                        words.add(line);
                }

                br.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }

            this.languages.put(language, words);
        }

        gui = new GUI();
    }

    public void switchLanguage() {
        language = languageNames.get((languageNames.indexOf(language) + 1) % languageNames.size());
    }
}
