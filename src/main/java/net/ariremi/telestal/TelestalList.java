package net.ariremi.telestal;

import net.md_5.bungee.api.chat.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TelestalList {
    Telestal plugin;
    public TelestalList(Telestal plugin){
        this.plugin = plugin;
    }

    //ページ内容
    public TextComponent PageContent(Integer page){
        String[] portal_list = PortalList();
        List<TextComponent> page_content = new ArrayList<>();
        for(int p = 0; p < GetPages(); p++){
            String content = "";
            if (p == GetPages() - 1){
                for(int c = 0; c < LastPage(); c++){
                    Integer num = Integer.valueOf((p*8)+c+1);
                    content = content + plugin.getConfig().getString("page_syntax").
                            replace("&","§").replace("<file>",portal_list[(p*8)+c]+"\n").replace("<num>",num.toString());
                }
            }else {
                for(int c = 0; c < 8; c++){
                    Integer num = Integer.valueOf((p*8)+c+1);
                    content = content + plugin.getConfig().getString("page_syntax").
                            replace("&","§").replace("<file>",portal_list[(p*8)+c]+"\n").replace("<num>",num.toString());
                }
            }

            //ページ操作処理（前）
            TextComponent page_previous = new TextComponent(plugin.getConfig().getString("page_previous").replace("&","§"));
            if(p == 0){
                page_previous.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(plugin.getConfig().getString("page_nothing").replace("&","§")).create()));
            }else{
                Integer prev_num = p-1;
                page_previous.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(plugin.getConfig().getString("page_previous_text").replace("&","§")).create()));
                page_previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/telestal list "+prev_num));
            }
            //ページ操作処理（次）
            TextComponent page_next = new TextComponent(plugin.getConfig().getString("page_next").replace("&","§"));
            if(p < GetPages() - 1){
                Integer next_num = p+2;
                page_next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(plugin.getConfig().getString("page_next_text").replace("&","§")).create()));
                page_next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/telestal list "+next_num));
            }else{
                page_next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(plugin.getConfig().getString("page_nothing").replace("&","§")).create()));
            }
            Integer next = Integer.valueOf(p+1);


            TextComponent page_message = new TextComponent();
            page_message.addExtra(plugin.getConfig().getString("page_start_line").
                    replace("&","§").replace("<now>",next.toString()).replace("<pages>",GetPages().toString()));
            page_message.addExtra("\n"+content);
            page_message.addExtra(page_previous);
            page_message.addExtra(plugin.getConfig().getString("page_end_line").replace("&","§"));
            page_message.addExtra(page_next);

            page_content.add(page_message);
        }
        return page_content.get(page);
    }

    //ページ数取得
    public Integer GetPages(){
        String[] portal_list = PortalList();
        Integer amount = portal_list.length;
        Integer pages;

        if(amount == 0){
            pages = 0;
        } else if(amount <= 8){
            pages = 1;
        }else {
            if(((double)amount/8) % 1 != 0){
                pages = ((int)amount/8) + 1;
            }else {
                pages = amount / 8;
            }
        }
        return pages;
    }

    //最終ページの項目数
    public Integer LastPage(){
        String[] portal_list = PortalList();
        Integer amount = portal_list.length;
        Integer last;

        if(amount <= 8){
            last = amount;
        }else if(((double)amount/8) % 1 != 0){
            last = amount - ((GetPages() - 1) * 8);
        }else {
            last = 8;
        }
        return last;
    }

    public String[] PortalList(){
        File File = new File(plugin.getDataFolder().getPath(),"portal");
        String[] files = File.list();
        List<String> ymlfile = new ArrayList<String>();
        for (String file : files) {
            if (file.substring(file.length() - 4).equals(".yml")) {
                ymlfile.add(file.replace(".yml", ""));
            }
        }
        return ymlfile.toArray(new String[ymlfile.size()]);
    }
}
