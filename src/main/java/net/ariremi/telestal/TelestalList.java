package net.ariremi.telestal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TelestalList {
    Telestal plugin;
    public TelestalList(Telestal plugin){
        this.plugin = plugin;
    }

    //ページ内容
    public String PageContent(Integer page){
        return "a";
    }

    //ページ数取得
    public Integer GetPages(){
        String[] portal_list = PortalList();
        Integer amount = portal_list.length;
        Integer pages;

        if(amount == 0){
            pages = 0;
        } else if(amount <= 6){
            pages = 1;
        }else {
            if(((double)amount/6) % 1 != 0){
                pages = ((int)amount/6) + 1;
            }else {
                pages = amount / 6;
            }
        }
        return pages;
    }

    //最終ページの項目数
    public Integer LastPage(){
        String[] portal_list = PortalList();
        Integer amount = portal_list.length;
        Integer last;

        if(amount <= 6){
            last = amount;
        }else if(((double)amount/6) % 1 != 0){
            last = amount - ((GetPages() - 1) * 6);
        }else {
            last = 6;
        }
        return last;
    }

    public String[] PortalList(){
        File File = new File(plugin.getDataFolder().getPath(),"portal");
        String[] files = File.list();
        List<String> ymlfile = new ArrayList<String>();
        for(int i = 0; i < files.length; i++){
            if(files[i].substring(files[i].length()-4).equals(".yml")){
                ymlfile.add(files[i].replace(".yml",""));
            }
        }
        return ymlfile.toArray(new String[ymlfile.size()]);
    }
}