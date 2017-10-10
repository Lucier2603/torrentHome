package com.Lucifer2603.torrentHome.common.dao;

import com.Lucifer2603.torrentHome.common.entity.MagnetEntity;

import java.util.List;

/**
 * @author zhangchen20
 */
public interface MagnetDao {

    void save(MagnetEntity magnet);

    List<MagnetEntity> findByTitle(String title);

    List<MagnetEntity> findByUrl(String url);
}
