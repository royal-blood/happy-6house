package com.royalblood.happy6house.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 450)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id",
                foreignKey = @ForeignKey(name = "FK_POST_USER"))
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @Column(nullable = false)
    private boolean deleted = false;



    @Builder // 빌더 패턴 클래스 생성, 생성자에 포함된 필드만 포함
    public Post(String title, String content, User user, PostCategory category) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.category = category;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
