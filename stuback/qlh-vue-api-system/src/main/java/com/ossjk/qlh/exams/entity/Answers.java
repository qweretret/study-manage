package com.ossjk.qlh.exams.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.ossjk.core.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 答卷（题）表
 * </p>
 *
 * @author admin
 * @since 2022-08-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Answers  extends BaseEntity<Answers> {

    private static final long serialVersionUID=1L;

      private String id;

      /**
     * 考试id
     */
      private String kid;

      /**
     * 班级id
     */
      private String cid;

      /**
     * 考试人id
     */
      private String sid;

      /**
     * 得分
     */
      private Integer score;

      /**
     * 考生的答案
     */
      private String answer;

      /**
     * 试卷id
     */
      private String expid;

      /**
     * 创建时间
     */
      private Date crtm;

      /**
     * 创建人
     */
      private String crer;

      /**
     * 修改时间
     */
      private Date mdtm;

      /**
     * 修改人
     */
      private String mder;

      /**
     * 答卷是否已批
     */
      private Integer isjudged;

    private Long startexam;


}
