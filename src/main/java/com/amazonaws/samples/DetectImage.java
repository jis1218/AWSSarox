package com.amazonaws.samples;

import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DetectImage {
	
	public void detectFaces(AmazonRekognition rekognitionClient, ByteBuffer bytebuffer, String collectionId) {
		
		Image image = new Image().withBytes(bytebuffer);
		
		DetectFacesRequest request = new DetectFacesRequest().withImage(image);
		
		DetectFacesResult result = rekognitionClient.detectFaces(request);
		
		List<FaceDetail> faceDetails = result.getFaceDetails();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		for(FaceDetail face : faceDetails) {
			try {
				System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
				
	}
	
	
	
	
	
	
	

}
