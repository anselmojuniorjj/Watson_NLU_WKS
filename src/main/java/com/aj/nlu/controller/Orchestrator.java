package com.aj.nlu.controller;

import com.aj.nlu.model.Dado;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.service.exception.RequestTooLargeException;
import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class Orchestrator {

    @RequestMapping(value = "/dados", method = RequestMethod.GET)
    public String dados(){
        return "insere";
    }


    @ResponseBody
    @RequestMapping(value = "/dados", method = RequestMethod.POST, produces = "application/json")
    public String orchestratorEntities(Dado dado) {

        // apiKey do NLU
       String apikey = dado.getApiKey();

        //ID do WKS que foi criado na IBM BlueMix
       String modelid = dado.getModelID();

       //Texto que vai ser analisado
       String texto = dado.getTexto();

        //Este método vai receber um texto e analisá-lo utilizando NLU e WKS (por padrão: pessoas, cidades, organozações, etc)

        try {
            // Invoke a Natural Language Understanding method
            IamOptions options = new IamOptions.Builder()
                    .apiKey(apikey)
                    .build();

            NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding("2018-03-16", options);
            service.setEndPoint("https://gateway.watsonplatform.net/natural-language-understanding/api");

            EntitiesOptions entities = new EntitiesOptions.Builder()
                    .sentiment(true)
                    .model(modelid)
                    .limit(1)
                    .build();

            Features features = new Features.Builder()
                    .entities(entities)
                    .build();

            AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                    .text(texto)
                    .features(features)
                    .build();

            AnalysisResults response = service
                    .analyze(parameters)
                    .execute();

            System.out.println(response);

            return response.toString();


        } catch (NotFoundException e) {
            // Handle Not Found (404) exception
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        } catch (RequestTooLargeException e) {
            // Handle Request Too Large (413) exception
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        } catch (ServiceResponseException e) {
            // Base class for all exceptions caused by error responses from the service
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        }


        return null;
    }
}
