//package net.zdsoft.framework.utils;
//
//import com.jacob.activeX.ActiveXComponent;
//import com.jacob.com.ComThread;
//import com.jacob.com.Dispatch;
//
//public class PDFCreatorUtils {
//
//		public static void main(String[] args) {
//			// TODO Auto-generated method stub
//			convert2pdf();
//		}
//
//		public static boolean convert2pdf() {
//			ActiveXComponent shellObj = null;
//			ActiveXComponent pdfCreatorQueue = null;
//			ActiveXComponent pdfCreatorObj = null;
//			String srcName = "C:\\doc\\2.docx";
//			String fullPath = "C:\\doc\\2.pdf";
////			System.out.println((new Timestamp(System.currentTimeMillis())).toLocalDateTime());
//			try {
//				ComThread.InitSTA();// 初始化COM线程
//				shellObj = new ActiveXComponent("Shell.Application");// 初始化shell程序
//				pdfCreatorQueue = new ActiveXComponent("PDFCreator.JobQueue");// 初始化exe程序
//				pdfCreatorObj = new ActiveXComponent("PDFCreator.PDFCreatorObj");
//
//				pdfCreatorQueue.invoke("Initialize");
//				System.out.println("pdfCreatorQueue init" );
//
//				//Dispatch.call(shellObj,"ShellExecute",srcName,"", "", "print", 0);
//				pdfCreatorObj.invoke("PrintFile", srcName);
//
//				System.out.println("Waiting for the job to arrive at the queue...");
//				if (!pdfCreatorQueue.invoke("WaitForJob", 10).getBoolean()) {
//					System.out.println("The print job did not reach the queue within " + " 10 seconds");
//				} else {
//					System.out.println(
//							"Currently there are " + pdfCreatorQueue.getPropertyAsInt("Count") + " job(s) in the queue");
//
//					// Getting job instance
//					Dispatch printJob = pdfCreatorQueue.invoke("NextJob").toDispatch();
//					Dispatch.call(printJob, "SetProfileByGuid", "DefaultGuid");
//					//job.SetProfileSetting("PdfSettings.Security.Enabled", "true");
//					Dispatch.call(printJob, "SetProfileSetting", "OpenViewer",false);
////					Dispatch.call(printJob, "SetProfileSetting", "BackgroundPage.Enabled",true);
//					Dispatch.call(printJob, "SetProfileSetting", "PdfSettings.FastWebView",true);
//					Dispatch.call(printJob, "SetProfileSetting", "PdfSettings.Security.AllowToCopyContent",true);
//					Dispatch.call(printJob, "SetProfileSetting", "PdfSettings.Security.AllowToEditTheDocument",false);
//					Dispatch.call(printJob, "SetProfileSetting", "Stamping.Enabled",false);
//					Dispatch.call(printJob, "SetProfileSetting", "Stamping.StampText","ZDsoft.net");
//					Dispatch.call(printJob, "SetProfileSetting", "Stamping.Color","#F7F7F7");
//
//
//
//					// Converting under "DefaultGuid" conversion profile
//					Dispatch.call(printJob, "ConvertTo", fullPath);
//					System.out.println("ConvertTo");
//					if (!Dispatch.get(printJob, "IsFinished").getBoolean()
//							|| !Dispatch.get(printJob, "IsSuccessful").getBoolean()) {
//						System.out.println("Could not convert the file: " + fullPath);
//					} else {
//						System.out.println("Job finished successfully");
//					}
//				}
//				return true;
//			} finally {
//				if (shellObj != null) {
//					shellObj.safeRelease();
//				}
//				if(pdfCreatorObj!=null){
//					pdfCreatorObj.safeRelease();
//				}
//				if (pdfCreatorQueue != null) {
//					pdfCreatorQueue.invoke("ReleaseCom");
//					pdfCreatorQueue.safeRelease();
//				}
//				ComThread.Release();
////				System.out.println((new Timestamp(System.currentTimeMillis())).toLocalDateTime());
//			}
//		}
//
//
//}
