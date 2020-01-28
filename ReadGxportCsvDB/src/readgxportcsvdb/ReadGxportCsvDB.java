/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readgxportcsvdb;

import controller.ReadAdjnodeCsv;
import controller.ReadIpPathCsv;
import controller.ReadIprtCsv;
import controller.ReadNodeBIpCsv;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import model.AdjNode;
import model.IpPath;
import model.Iprt;
import model.NodeBIp;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author P05144
 */
public class ReadGxportCsvDB {
    private static String _db_dir=null;
    public static String getDb_dir() {
        return _db_dir;
    }

    public static void setDb_dir(String _db_dir) {
        ReadGxportCsvDB._db_dir = _db_dir;
    }
    /**
     * PROGRMAMA PARA LEER LA BASE DE DATOS CSV
     * Parametros rnc = nombre de RNC
     * n= nodos de la rnc (true or false) si se desea conocer los nodeb de la rnc
     * iprt (true or false) si se desea conocer la vrf, srn,sn de las rnc
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Options options = new Options();
        CommandLine cmd = null;
        Boolean showHelpMessage = false;
        Boolean showVersion = false;
        String _rnc = null;
        boolean inputNodes =false;
        boolean iprtSrn=false;
        boolean iprtSn=false;
        boolean iprtPort=false;
        boolean iprtVRF=false;
        
        short _srn=-1;
        short _sn=-1;
        //short _port=-1;
         try {
            options.addOption( "v", "version", false, "display version" );
            options.addOption(Option.builder("rnc")
                    .longOpt( "rnc" )
                    .desc( "(RNC) to create GOU Script")
                    .hasArg()
                    .argName( "RNC" ).build());
            options.addOption(Option.builder("srn")
                    .longOpt( "srn" )
                    .desc( "(RNC) SRN")
                    .hasArg()
                    .argName( "SRN" ).build());
            options.addOption(Option.builder("sn")
                    .longOpt( "sn" )
                    .desc( "(RNC) sn")
                    .hasArg()
                    .argName( "SN" ).build());
            /*options.addOption(Option.builder("port")
                    .longOpt( "port" )
                    .desc( "(RNC) port")
                    .hasArg()
                    .argName( "PORT" ).build());*/
            options.addOption( "n", "nodes", false, "See RNC nodeB" );
            options.addOption( "iprtSrn", "iprtSrn", false, "See RNC IPRT.csv SRN" );
            options.addOption( "iprtSn", "iprtSn", false, "See RNC IPRT.csv SN" );
            options.addOption( "iprtPort", "iprtPort", false, "See RNC IPRT.csv PORT" );
            options.addOption( "iprtVRF", "iprtVRF", false, "See RNC IPRT.csv VRF" );
            options.addOption( "h", "help", false, "show help" );
            options.addOption( Option.builder("db")
                    .longOpt( "input-db-directory" )
                    .desc( "DB path directory ")
                    .hasArg()
                    .argName( "INPUT_DB_DIRECTORY" ).build());
            //Parse command line arguments
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse( options, args);
            if( cmd.hasOption("h")){
                showHelpMessage = true;
            }

            if( cmd.hasOption("v")){
                showVersion = true;
            }
            if(cmd.hasOption('n')){
                inputNodes = true;
                
            }
            if(cmd.hasOption("iprtSrn")){
                iprtSrn = true;
                
            }
            if(cmd.hasOption("iprtSn")){
                iprtSn = true;
                
            }
            if(cmd.hasOption("iprtPort")){
                iprtPort = true;
                
            }
            if(cmd.hasOption("iprtVRF")){
                iprtVRF = true;
                
            }
            if(cmd.hasOption("rnc")){
                
              _rnc= cmd.getOptionValue("rnc").toUpperCase();
              
                
            }
            if(cmd.hasOption("srn")){
                
              _srn = Short.parseShort(cmd.getOptionValue("srn").toUpperCase());
              
                
            }
            if(cmd.hasOption("sn")){
                
              _sn = Short.parseShort(cmd.getOptionValue("sn").toUpperCase());
              
                
            }
            /*if(cmd.hasOption("port")){
                
              _port = Short.parseShort(cmd.getOptionValue("port").toUpperCase());
              
                
            }*/
            if(cmd.hasOption("db")){
               
              _db_dir= cmd.getOptionValue("db");
                
            }
         }catch (ParseException ex) {
            //System.out.println("ParseException ERROR GENERAL "+ex.getMessage().toString());
           
            System.err.println("ParseException ERROR GENERAL "+ex.getMessage());
            System.exit(1);
        }
         
         if(showVersion == true ){
                System.out.println("1.0.0 \nreadgxportcsvdb Copyright (c)  Telefonica VENEZUELA ");
                //System.out.println("1.0.0");
                //System.out.println("Copyright (c)"+executionTime.getDate()+" Telefonica VENEZUELA");
                System.exit(0);
            }
            
            //show help
            if( showHelpMessage == true || 
                  _rnc == null || _db_dir ==null
                 ){
                     HelpFormatter formatter = new HelpFormatter();
                     String header = "READ RNC and RNC NODEB\n\n";
                     String footer = "\n";
                     footer += "Examples: \n";
                     footer += "java -jar readgxportcsvdb.jar -rnc RNC_NAME -i NODE_LIST\n";
                     formatter.printHelp( "java -jar readgxportcsvdb.jar -h", header, options, footer );
                     System.exit(0);
            }
           try {
            
            if(!inputNodes && !iprtSrn && !iprtSn && !iprtPort && !iprtVRF){
                //Conocer las rnc cargadas en la base de datos
                List<AdjNode> list = ReadAdjnodeCsv.getAdjRNC(_rnc).stream()
                        .collect(
                                collectingAndThen(
                                        toCollection(()
                                                -> new TreeSet<>(comparing(AdjNode::getFilename))),
                                         ArrayList::new)
                        );
                list.forEach((t) -> {
                    String[] words = t.getFilename().split("_");
                    System.out.println(words[1]);
                });
                
            }
            
            if(inputNodes){
                
                    //Coonocer los NOdeb de la RNC

                    List<AdjNode> listAdj = ReadAdjnodeCsv.getAdjNodeRnc(_rnc).stream()
                            .collect(
                                    collectingAndThen(
                                            toCollection(()
                                                    -> new TreeSet<>(comparing(AdjNode::getName))),
                                            ArrayList::new)
                            );
                    Iterator it = listAdj.iterator();
                    while (it.hasNext()){
                        AdjNode adjNode = (AdjNode) it.next();
                        if (adjNode.getName().startsWith("U")) {
                            
                            List<NodeBIp> nodeBip = ReadNodeBIpCsv.getNodeBDstip(_rnc, adjNode.getNodeBid());
                            
                            if(!nodeBip.isEmpty()){
                                String[] nodeBSplip= nodeBip.get(0).getNBTRANTP().split("_");
                                System.out.println(adjNode.getName()+"-"+nodeBSplip[0]);
                                
                                //nodeBip.forEach(System.out::println);
                            }/*else{
                                System.out.println(adjNode.getName() + "-" + adjNode.getTranst());
                            }*/
                                
                        }
                    }
                    /*listAdj.forEach((t) -> {
                        if (t.getName().startsWith("U")) {
                            //System.out.println(t.getName() + "-" + t.getTranst());
                            //Para saber si el nodo es ATM
                            List<NodeBIp> nodeBip = ReadNodeBIpCsv.getNodeBDstip(_rnc, t.getAni());
                        }
                    });*/

                
            }    

           
            
            
            //CONOCER EL SRN IPRT DE LA RNC
            if(iprtSrn){        
                    List<Iprt> _iprtSrn = ReadIprtCsv.getIprtNode(_rnc).stream()
                            .collect(
                                    collectingAndThen(
                                            toCollection(()
                                                    -> new TreeSet<>(comparing(Iprt::getSrn))),
                                            ArrayList::new)
                            );;
                    _iprtSrn.forEach((t) -> {
                        
                            System.out.println(t.getSrn());
                        
                    });
                    
            }
            //CONOCER EL SN IPRT DEL SRN de la RNC
            if(iprtSn){
                if(_srn != -1){
                    List<Iprt> _iprtSn = ReadIprtCsv.getIprtSN(_rnc,_srn).stream()
                            .collect(
                                    collectingAndThen(
                                            toCollection(()
                                                    -> new TreeSet<>(comparing(Iprt::getSn))),
                                            ArrayList::new)
                            );
                    _iprtSn.forEach((t) -> {
                        
                            System.out.println(t.getSn());
                        
                    });
                }
            }
            //CONOCER EL PORT IPRT DEL SN-SRN DE LA RNC
            if(iprtPort){
                if(_srn != -1 &&_sn!= -1){
                    List<Iprt> listIprt = ReadIprtCsv.getIprtPortNexthop(_rnc,_srn,_sn).stream()
                            .collect(
                                    collectingAndThen(
                                            toCollection(()
                                                    -> new TreeSet<>(comparing(Iprt::getNextpn))),
                                            ArrayList::new)
                            );;
                    listIprt.forEach((t) -> {
                        
                            System.out.println(t.getNextpn());
                        
                    });
                }
            }
            //CONOCER EL VRF IPRT DE LA RNC
            if(iprtVRF){        
               
                    List<IpPath> listIprt = ReadIpPathCsv.getIpPathNode(_rnc).stream()
                            .collect(
                                    collectingAndThen(
                                            toCollection(()
                                                    -> new TreeSet<>(comparing(IpPath::getIPADDR))),
                                            ArrayList::new)
                            );;
                    listIprt.forEach((t) -> {
                        
                            System.out.println(t.getIPADDR());
                        
                    });
            }
        } catch (IOException e) {
            System.out.println("" + e);
        }
            
    }
    
}
