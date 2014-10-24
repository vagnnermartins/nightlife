package br.com.nightlife.util;

import java.text.DecimalFormat;

public class DistanciaUtil {
	public static String distanciaEmMetrosPorExtenso(double distanciaEmMetros) {
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#");
		double kms = distanciaEmMetros / 1000;
		kms = Double.parseDouble(df.format(kms));
		double metros = distanciaEmMetros % 1000;
		metros = Double.parseDouble(df.format(metros));
		if(kms < 1){
			sb.append("< 1 Km");
		}else{
			sb.append(Double.valueOf(kms).intValue());
			sb.append(" Km");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param lat1
	 * @param long1
	 * @param lat2
	 * @param long2
	 * @return Distância em metros
	 */
	public static double calcularDistanciaEntreDoisPontos(double lat1, double long1, 
			double lat2, double long2) {
		long R = 6378137L;
		double dLat = rad(lat2 - lat1);
		double dLong = rad(long2 - long1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(rad(lat1)) * Math.cos(rad(lat2))
				* Math.sin(dLong / 2) * Math.sin(dLong / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}

	private static double rad(double lat) {
		return lat * Math.PI / 180;
	}
}
