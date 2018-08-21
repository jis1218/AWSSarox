package com.amazonaws.samples;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.DeleteFacesRequest;
import com.amazonaws.services.rekognition.model.DeleteFacesResult;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.InvalidParameterException;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.amazonaws.services.rekognition.model.SearchFacesRequest;
import com.amazonaws.services.rekognition.model.SearchFacesResult;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;


public class AWSPro{	
	
	public static void main(String args[]) throws IOException{
		ScheduledExecutorService executor;
		DetectImage detectImage = new DetectImage();
		Webcam webcam = Webcam.getDefault();
		System.out.println(webcam.getName());			
		webcam.setCustomViewSizes(new Dimension[] {WebcamResolution.FHD.getSize()});
		webcam.setViewSize(WebcamResolution.FHD.getSize());
		webcam.open(true);
		Sarox cv = new Sarox();
		cv.setVisible(true);
		//System.out.println(webcam.getImage().getWidth());
		String photo = "pic3.jpg";
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//OpenCV openCV = new OpenCV();
		//openCV.loadCascade();
		
//		BufferedImage originalImage;
//		BufferedImage targetImage;
//		originalImage = ImageIO.read(new File("/home/insup/Pictures/beatles.jpg"));
//		targetImage = ImageIO.read(new File("/home/insup/Pictures/beatles.jpg"));
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(originalImage, "jpg", baos);
//		baos.flush();
//		byte[] imageInByte = baos.toByteArray();
//		baos.reset(); //baos�� reset���� ������ baos�� ���� �����Ͱ� �״�� ���Ƿ� ���̰� �ȴ�.
//		ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
//		ImageIO.write(targetImage, "jpg", baos);
//		baos.flush();		
//		byte[] targetimageInByte = baos.toByteArray();
//		System.out.println(imageInByte.length);
//		System.out.println(targetimageInByte.length);
//		baos.close();
//		baos1.close();
//		ByteBuffer imageBytes1 = ByteBuffer.wrap(imageInByte);
//		ByteBuffer imageBytes2 = ByteBuffer.wrap(targetimageInByte);
		//ClassLoader classLoader = new AWSPro().getClass().getClassLoader();
		
		
		AmazonRekognition rekognitionClient = AWSAuth.getClient();
		//BufferedImage bufferedImage = null;
		
		executor = Executors.newSingleThreadScheduledExecutor();
		
		int i=0;
		//while(true){
			
		
		executor.scheduleAtFixedRate(()->{
			
				BufferedImage bufferedImage = webcam.getImage();
				
				//BufferedImage bufferedImage = ImageIO.read(new File("/home/insup/Pictures/beatles.jpg"));
				new Thread(()->{					
					try {
						ByteArrayOutputStream bs = new ByteArrayOutputStream();
						ImageIO.write(bufferedImage, "jpg", bs);
						bs.flush();
						byte[] imageIn = bs.toByteArray();
						
						ByteBuffer imageBytes3 = ByteBuffer.wrap(imageIn);		
						//System.out.println("사진 변환 완");

						//compareFace(imageBytes1, imageBytes2, rekognitionClient);
						//detectImage.detectFaces(rekognitionClient, imageBytes3, "MyCollection");
						searchFacesMatch(rekognitionClient, imageBytes3, "MyCollection");
						//System.out.println("============== 비교 후");
					}catch(Exception e) {
						
					}
					
				}).start();

		
		}, 0, 250, TimeUnit.MILLISECONDS);
		
		
		
		//executor로 구현해봐야 함
		//executor.scheduleAtFixedRate(command, initialDelay, period, unit)
			
		
					
//					i++;
//					
//					if(i==10) {
//						try {
//							Mat mat = Utils.bufferedImageToMat(bufferedImage);
//							Imgcodecs.imwrite("/home/insup/Pictures/hey.jpg", mat);
//							System.out.println("I got hey!!!");
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
				//}
						
		
	}
	
	private static void searchFacesMatch(AmazonRekognition rekognitionClient, ByteBuffer imageBytes1, String collectionId) throws Exception{
		//ObjectMapper objectMapper = new ObjectMapper(); //JSon�� string���� �ٲ��ִµ� ������ �Ѵ�.
		
		Image image = new Image().withBytes(imageBytes1);
		
		SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
				.withCollectionId(collectionId)
				.withImage(image)
				.withFaceMatchThreshold(70F)
				.withMaxFaces(3);
				
				
		List<FaceMatch> faceImageMatches = null;
		
		try{
		SearchFacesByImageResult searchFacesByImageResult = rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
		faceImageMatches = searchFacesByImageResult.getFaceMatches();
		}catch(Exception e){
			System.out.println("사람 얼굴이 아니거나 식별할 수 없습니.");
		}
		
		
		if (faceImageMatches.size()==0){
			System.out.println("해당 얼굴을 찾을 수 없습니.");
			return;
		}
		
		for(FaceMatch face: faceImageMatches){
			
				//System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
				System.out.println(face.getSimilarity());
			
		}		
	}

	private static void createCollection(AmazonRekognition rekognitionClient, ByteBuffer imageByte){
		String collectionId = "MyCollection";
		
		Image image = new Image().withBytes(imageByte);
		
		IndexFacesRequest indexFacesRequest = new IndexFacesRequest().withImage(image).withCollectionId(collectionId)
				.withExternalImageId("sample").withDetectionAttributes("ALL");
		
		IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);
		
		List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
		for(FaceRecord faceRecord : faceRecords){
			System.out.println("face detected : Faceid is " + faceRecord.getFace().getFaceId());
			System.out.println("face detected : Faceid is " + faceRecord.getFaceDetail().toString());
		}	
	}
	
	private static void searchFace(AmazonRekognition rekognitionClient) throws IOException{
		String collectionID = "MyCollection";
		String faceId = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		SearchFacesRequest searchFacesRequest = new SearchFacesRequest().withCollectionId(collectionID)
				.withFaceId(faceId).withFaceMatchThreshold(70F).withMaxFaces(2);
		
		SearchFacesResult searchFacesByIdResult = rekognitionClient.searchFaces(searchFacesRequest);
		
		System.out.println("Face matching faceId " + faceId);
		List<FaceMatch> faceImageMatches = searchFacesByIdResult.getFaceMatches();
		for(FaceMatch face: faceImageMatches){
			System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
		}
		
	}
	
	private static void compareFace(ByteBuffer imageBytes1, ByteBuffer imageBytes2, AmazonRekognition rekognitionClient){
		System.out.println("�� ����");
		CompareFacesRequest cfRequest = new CompareFacesRequest().withTargetImage(new Image().withBytes(imageBytes2))
				.withSourceImage(new Image().withBytes(imageBytes1));
		System.out.println("Ŭ���̾�Ʈ ����");
		
		CompareFacesResult cfResult = rekognitionClient.compareFaces(cfRequest);
		
		System.out.println("=====Ŭ���̾�Ʈ �Ϸ�");
		List<CompareFacesMatch> result = cfResult.getFaceMatches();
		List<ComparedFace> unmatchedResult = cfResult.getUnmatchedFaces();
		
		for(CompareFacesMatch cfm : result){
			System.out.println("Same person : " + cfm.toString());
		}
		
		for(ComparedFace cf : unmatchedResult){
			System.out.println("Not same person : " + cf.toString());
		}
	}
	
	private static void displayFaceDetail(ByteBuffer imageBytes, AmazonRekognition rekognitionClient, String photo){
		DetectFacesRequest dfRequest = new DetectFacesRequest().withImage(new Image().withBytes(imageBytes))
				.withAttributes("ALL");
		
		try{
			DetectFacesResult result = rekognitionClient.detectFaces(dfRequest);
			List<FaceDetail> faceDetails = result.getFaceDetails();
			
			System.out.println("Detected Face for " + photo);
			for(FaceDetail facedetail: faceDetails){
				System.out.println("Face Detail: " + facedetail.toString());
			}
		}catch(AmazonRekognitionException e){
			e.printStackTrace();
		}
	}
	
	private static void displayLabel(ByteBuffer imageBytes, AmazonRekognition rekognitionClient, String photo){
		DetectLabelsRequest request = new DetectLabelsRequest()
				.withImage(new Image().withBytes(imageBytes))
				.withMaxLabels(10)
				.withMinConfidence(77F);
		
		try{
			DetectLabelsResult result = rekognitionClient.detectLabels(request);
			List<Label> labels = result.getLabels();
			
			System.out.println("Detected labels for " + photo);
			for(Label label : labels){
				System.out.println(label.getName() + ": " + label.getConfidence().toString());
			}
		}catch(AmazonRekognitionException e){
			e.printStackTrace();
		}
	}

}
