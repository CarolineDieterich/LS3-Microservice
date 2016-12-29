package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import de.andreasschoknecht.LS3.*;

public class Ls3Algorithm {

	public ArrayList<Map> execute(String pnmlPath, int k, float theta) {

		LS3 ls3 = new LS3();
		QueryAllResult queryAllResult = ls3.queryKAll(pnmlPath, k, theta);

		/*
		 * QueryAllResult besteht aus einer ArrayList<QueryResult>. Jedes
		 * QueryResult besteht aus einem LS3Document sowie einer
		 * ArrayList<LS3Document>, die die LS3Documents �hnlicher Netze aus der
		 * Collection enth�lt. Ein LS3Document enth�lt u.a. den Pfad zum
		 * betrachteten Petrinetz, aus dem sich der Name des Netzes extrahieren
		 * l�sst.
		 */

		ArrayList<QueryResult> resultList = queryAllResult.getResults();
		LS3Document ls3Document;
		String fileName = new String("");
		String fileNameSimilar = new String("");
		String similarPetrinets = new String("");
		ArrayList<LS3Document> results;
		ArrayList<Map> result = new ArrayList<Map>();
		HashMap<String, String> queryResultMap;

		try {
			int i = 0;
			while (i < resultList.size()) {

				/*
				 * Den Namen des in diesem LS3Document betrachteten Petrinetzes
				 * aus dem Dateipfad extrahieren und in fileName speichern
				 */
				ls3Document = resultList.get(i).getQuery();
				fileName = extractFileName(ls3Document.getPNMLPath());

				/*
				 * Erstelle f�r alle QueryResults in queryAllResult eine Hashmap
				 * mit dem Namen des aktuellen Petrinetzes sowie den Namen der
				 * �hnlichen Netze
				 */
				queryResultMap = new HashMap<String, String>();

				/*
				 * Die LS3Documents des einzelnen QueryResults (die die
				 * �hnlichen Netze repr�sentieren) abspeichern
				 */
				results = resultList.get(i).getResults();

				/*
				 * Die LS3Documents durchgehen und Namen des jew. Netzes
				 * abspeichern
				 */
				int o = 0;
				similarPetrinets = "";
				while (o < results.size()) {

					/*
					 * alle Namen �hnlicher Petrinetze in einem String durch ","
					 * getrennt aneinanderreihen wenn fileNameSimilar bisher
					 * leer, dann ohne , anf�gen
					 */
					fileNameSimilar = extractFileName(results.get(o).getPNMLPath());
					if (similarPetrinets.equals("")) {
						similarPetrinets = fileNameSimilar;
					} else {
						similarPetrinets = similarPetrinets + "," + fileNameSimilar;
					}

					o++;
				}
				queryResultMap.put("name", fileName);
				queryResultMap.put("similar petrinets", similarPetrinets);
				result.add(queryResultMap);
				i++;
			}

		} catch (Exception e) {
			System.err.println("====================================================");
			System.err.println("Fehler in Ls3Algorithm.java");
			System.err.println("====================================================");
			e.printStackTrace();
		}

		return result;
	}

	private String extractFileName(String string) {
		String result = string.substring(string.lastIndexOf("\\") + 1);
		result = FilenameUtils.removeExtension(result);
		return result;
	}
}
