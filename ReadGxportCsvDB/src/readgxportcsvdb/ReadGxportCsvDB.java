/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readgxportcsvdb;

import controller.ReadAdjnodeCsv;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.TreeSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import model.AdjNode;
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
         try {
            options.addOption( "v", "version", false, "display version" );
            options.addOption(Option.builder("rnc")
                    .longOpt( "rnc" )
                    .desc( "(RNC) to create GOU Script")
                    .hasArg()
                    .argName( "RNC" ).build());
            options.addOption( "n", "nodes", false, "See RNC nodeB" );
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
            if(!inputNodes){
                try{
                    List<AdjNode> list = ReadAdjnodeCsv.getAdjRNC(_rnc).stream()
                                                    .collect(
                                                         collectingAndThen(
                                                                 toCollection(() -> 
                                                                         new TreeSet<>(comparing(AdjNode::getFilename)))
                                                                 ,ArrayList::new)
                                                    );
                    list.forEach((t) -> {
                        String [] words =t.getFilename().split("_");
                        System.out.println(words[1]);
                    });
                }catch(IOException e){
                    System.out.println(""+e);
                }
            }else{
                 try{
                    List<AdjNode> list = ReadAdjnodeCsv.getAdjNodeRnc(_rnc).stream()
                                                    .collect(
                                                         collectingAndThen(
                                                                 toCollection(() -> 
                                                                         new TreeSet<>(comparing(AdjNode::getName)))
                                                                 ,ArrayList::new)
                                                    );
                    list.forEach((t) -> {
                        if(t.getName().startsWith("U"))
                        System.out.println(t.getName() +" - "+ t.getTranst());
                    });
                }catch(IOException e){
                    System.out.println(""+e);
                }
            }
    }
    
}
