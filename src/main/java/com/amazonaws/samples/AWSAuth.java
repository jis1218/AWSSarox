package com.amazonaws.samples;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;

public class AWSAuth {
	
	public static AmazonRekognition getClient() {
		
		AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_WEST_2)
					.withCredentials(new AWSStaticCredentialsProvider(credentials))
					.build();
		
		System.out.println(credentials.toString());
		System.out.println(credentials.getAWSAccessKeyId());
		System.out.println(credentials.getAWSSecretKey());
		
		return rekognitionClient;
		
	}
}
