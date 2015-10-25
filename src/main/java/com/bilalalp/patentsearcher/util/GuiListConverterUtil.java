package com.bilalalp.patentsearcher.util;

import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.dto.SiteInfoDto;
import com.bilalalp.patentsearcher.dto.StopWordInfoDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.entity.StopWordInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public final class GuiListConverterUtil {

    private GuiListConverterUtil() {
        //Util Class
    }

    public static ObservableList<KeywordInfoDto> getKeywordInfoObservableList(final List<KeywordInfo> keywordInfoList) {

        final ObservableList<KeywordInfoDto> keywordInfoDtoObservableList = FXCollections.observableArrayList();
        keywordInfoDtoObservableList.addAll(keywordInfoList.stream().map(keywordInfo -> new KeywordInfoDto(keywordInfo.getId(), keywordInfo.getKeyword())).collect(Collectors.toList()));
        return keywordInfoDtoObservableList;
    }

    public static ObservableList<StopWordInfoDto> getStopWordInfoObservableList(final List<StopWordInfo> stopWordInfoList) {
        final ObservableList<StopWordInfoDto> keywordInfoDtoObservableList = FXCollections.observableArrayList();
        keywordInfoDtoObservableList.addAll(stopWordInfoList.stream().map(keywordInfo -> new StopWordInfoDto(keywordInfo.getId(), keywordInfo.getWord())).collect(Collectors.toList()));
        return keywordInfoDtoObservableList;
    }

    public static ObservableList<SiteInfoDto> getSiteInfoObservableList(final List<SiteInfo> keywordInfoList) {

        final ObservableList<SiteInfoDto> keywordInfoDtoObservableList = FXCollections.observableArrayList();
        keywordInfoDtoObservableList.addAll(keywordInfoList.stream().map(keywordInfo -> new SiteInfoDto(keywordInfo.getId(), keywordInfo.getSiteName(), keywordInfo.getSiteAddres())).collect(Collectors.toList()));
        return keywordInfoDtoObservableList;
    }
}
