package com.mifel.test.dto.post;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PostDto {
	private Long postId;
	private String title;
	private String body;
	private String path;
	private Boolean deleted;
	private Long createdBy;
	private Long modifiedBy;
	private LocalDateTime created;
	private LocalDateTime modified;
}
