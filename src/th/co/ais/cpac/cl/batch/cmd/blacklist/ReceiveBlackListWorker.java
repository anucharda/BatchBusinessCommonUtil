package th.co.ais.cpac.cl.batch.cmd.blacklist;

import java.io.File;

import th.co.ais.cpac.cl.batch.ConstantsBatchReceiveResult;
import th.co.ais.cpac.cl.batch.util.FileUtil;
import th.co.ais.cpac.cl.batch.util.PropertiesReader;
import th.co.ais.cpac.cl.common.Context;

public class ReceiveBlackListWorker {

	public static void main(String[] args) {
		Context context = new Context();
		//context.initailLogger(name, header); 
		try{
			
			context.initailLogger("LoggerReceive", "ReceiveBlackListWorker");
			// TODO Auto-generated method stub
			context.getLogger().info("----------------------- Start ReceiveBlackListWorker -----------------------");
			context.getLogger().info("Load configure....");
			PropertiesReader reader = new PropertiesReader("th.co.ais.cpac.cl.batch.properties.resource","SystemConfigPath");
		
			String jobType=args[0];//From Parameter
		
			//Find sync file on path.
			String inboundSyncPath="" ;
			String inboundDataPath="" ;
			String processPath="";
			boolean doProcess=true;
			System.out.println("jobType ->"+jobType);
			if(ConstantsBatchReceiveResult.blacklistJobType.equals(jobType)){
				inboundSyncPath= reader.get("blacklist.inboundSyncPath");
				inboundDataPath =reader.get("blacklist.inboundDataPath");
				processPath = reader.get("blacklist.processPath");
			}else{
				doProcess=false;
				context.getLogger().info("not found job type : "+jobType);
			}
			context.getLogger().info("inboundSyncPath : "+inboundSyncPath);
			if(doProcess){
				File[] files = FileUtil.getAllFilesThatMatchFilenameExtensionAscendingOrder(inboundSyncPath, "sync");

				String syncFileName = "";

				if(files!=null && files.length>0){
					context.getLogger().info("Sync file size --> "+files.length);
					int totalThread=Integer.parseInt(reader.get("sff.total.thread"));
					int loopCnt=files.length;
					if(files.length>totalThread){
						loopCnt=totalThread;
					}
					//Loop for do all sync file.
					for(int i=0; i<loopCnt; i++){
						String doPath = processPath;
						String syncFile = files[i].getPath();
						syncFileName = files[i].getName();
						context.getLogger().info("Sync file is --> "+syncFile);
						
						String[] fileNames=new String[1];
						fileNames[0]=syncFileName.replaceAll(".sync",ConstantsBatchReceiveResult.blacklistExt);
						
						for(int j=0; j<fileNames.length; j++){
							File source = new File(inboundDataPath+"/"+fileNames[j]);
							if(source.exists()){
								File dest = new File(processPath+"/"+fileNames[j]);
								FileUtil.copyFile(source, dest);
								context.getLogger().info("Copy file to process directory successed --> "+fileNames[j]);
								//Delete source file after copy to process directory completed.
								if(source.delete()){
									context.getLogger().info("Delete file from inbound directory successed --> "+source.getParent());
								}else{
									throw new Exception("Error occur delete file from inbound directory --> "+source.getParent());
								}
							}else{
								//Skip to next file.
								continue;
							}
						}
						//Copy sync file to process directory.
						File sourceSyncFile = new File(syncFile);
						File destSyncFile = new File(processPath+"/"+syncFileName);
						FileUtil.copyFile(sourceSyncFile, destSyncFile);
						//Delete source file after copy to process directory completed.
						if(sourceSyncFile.delete()){
							context.getLogger().info("Delete file from inbound directory successed --> "+sourceSyncFile.getParent());
						}else{
							throw new Exception("Error occur delete file from inbound directory --> "+sourceSyncFile.getParent());
						}
						//New thread for execute process.
						new Thread ( () -> execute(context, jobType,fileNames[0],doPath) ).start();
						//execute(context, jobType,fileNames,doPath);
					}

				}else{
					context.getLogger().info("Not found sync file in directory --> "+inboundSyncPath);
				}
			}

			
			context.getLogger().info("----------------------- End ReceiveSFFWorker ----------------------- ");
		}catch(Exception e){
			context.getLogger().info("Error->"+e.getMessage()+": "+e.getCause().toString());
		}
	}
	
	public static void execute(Context context,String jobType,String fileName,String processPath){
		 context.getLogger().info("Start UpdateSFFResultProcess Execute....");
		 context.getLogger().info("Trigger Update SSF Result Process....");
		 new UpdateBlacklistResultProcess().executeProcess(context,jobType,fileName,processPath);
		 context.getLogger().info("End UpdateSFFResultProcess Execute....");
	}
	
	
}