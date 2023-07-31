package io.pisceshub.muchat.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author xiaochangbai
 * @date 2023-07-08 11:37
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageResp<T> implements Serializable {

  private List<T> list;

  private boolean hasNext;

  public static <T> PageResp<T> empty() {
    return (PageResp<T>) PageResp.builder().hasNext(false).list(Collections.emptyList()).build();
  }

  public static <T> PageResp<T> toPage(List<T> memberList, boolean hasNext) {
    PageResp<T> resp = new PageResp<T>();
    resp.setList(memberList);
    resp.setHasNext(hasNext);
    return resp;
  }
}
