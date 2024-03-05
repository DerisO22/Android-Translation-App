<p align="center"><strong>Android Translation App 🚀 </strong></p>

![Translation Image](https://t3.ftcdn.net/jpg/04/36/43/12/360_F_436431209_IrKCuIPj2FubrYDZpYLZPZxDShsqSrwa.jpg)

<p align="center"><strong>Welcome to my Translation App</strong>
<p align="center"><strong>My app allows users to translate text, speech, and images from a wide variety of different languages.<br>Navigate through the different windows by scrolling
or choosing a window from the home page.<br>Using Firebase ML and other language APIs, you will be able to accurately translate between 50+ langauges.</strong>
<br><br><br>

```
//Populate the List With All Available Languages FireBase Has Available
private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();

        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        for(String languageCode: languageCodeList){
            String languageTitle = new Locale(languageCode).getDisplayLanguage();
            Log.d(TAG, "LoadAvailableLanguages: languageCode: "+languageCode);
            Log.d(TAG, "LoadAvailableLanguages: languageCode: "+languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode, languageTitle);
            languageArrayList.add(modelLanguage);
        }
    }
```
