/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readgxportcsvdb;

import controller.ReadAdjnodeCsv;
import controller.ReadIprtCsv;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.TreeSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import model.AdjNode;
import model.Iprt;
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
        boolean port=false;
        boolean iprtVRF=false;
         try {
            options.addOption( "v", "version", false, "display version" );
            options.addOption(Option.builder("rnc")
                    .longOpt( "rnc" )
                    .desc( "(RNC) to create GOU Script")
                    .hasArg()
                    .argName( "RNC" ).build());
            options.addOption( "n", "nodes", false, "See RNC nodeB" );
            options.addOption( "iprtSrn", "iprtSrn", false, "See RNC IPRT.csv SRN" );
            options.addOption( "iprtSn", "iprtSrn", false, "See RNC IPRT.csv SN" );
            options.addOption( "port", "iprtSrn", false, "See RNC IPRT.csv PORT" );
            options.addOption( "iprtVRF", "iprtSrn", false, "See RNC IPRT.csv VRF" );
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
            if(cmd.hasOption("port")){
                port = true;
                
            }
            if(cmd.hasOption("iprtVRF")){
                iprtVRF = true;
                
            }
            if(cmd.hasOption("rnc")){
                
              _rnc= cmd.getOptionValue("rnc").toUpperCase();
              
                
            }
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
                
            
            
            if(inputNodes){
                
                    //Coonocer los NOdeb de la RNC

                    List<AdjNode> listAdj = ReadAdjnodeCsv.getAdjNodeRnc(_rnc).stream()
                            .collect(
                                    collectingAndThen(
                                            toCollection(()
                                                    -> new TreeSet<>(comparing(AdjNode::getName))),
                                            ArrayList::new)
                            );
                    listAdj.forEach((t) -> {
                        if (t.getName().startsWith("U")) {
                            System.out.println(t.getName() + " - " + t.getTranst());
                        }
                    });

                
            }    

                
            
            
            //CONOCER EL SRN IPRT DE LA RNC
            if(iprtSrn){        
                    List<Iprt> listIprt = ReadIprtCsv.getIprtNode(_rnc).stream()
                            .collect(
                                    collectingAndThen(
                                            toCollection(()
                                                    -> new TreeSet<>(comparing(Iprt::getSrn))),
                                            ArrayList::new)
                            );;
                    listIprt.forEach((t) -> {
                        
                            System.out.println(t.getSrn());
                        
                    });
            }
            //CONOCER EL SN IPRT DE LA RNC
            if(iprtSn){        
                    List<Iprt> listIprt = ReadIprtCsv.getIprtNode(_rnc).stream()
                            .collect(
                                    collectingAndThen(
                                            toCollection(()
                                                    -> new TreeSet<>(comparing(Iprt::getSn))),
                                            ArrayList::new)
                            );;
                    listIprt.forEach((t) -> {
                        
                            System.out.println(t.getSn());
                        
                    });
            }
            //CONOCER EL PORT IPRT DE LA RNC
            if(port){        
                    List<Iprt> listIprt = ReadIprtCsv.getIprtNode(_rnc).stream()
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
            //CONOCER EL VRF IPRT DE LA RNC
            if(iprtVRF){        
                    List<Iprt> listIprt = ReadIprtCsv.getIprtNode(_rnc).stream()
                            .collect(
                                    collectingAndThen(
                                            toCollection(()
                                                    -> new TreeSet<>(comparing(Iprt::getDstip))),
                                            ArrayList::new)
                            );;
                    listIprt.forEach((t) -> {
                        
                            System.out.println(t.getDstip());
                        
                    });
            }
        } catch (IOException e) {
            System.out.println("" + e);
        }
            
    }
    
}
