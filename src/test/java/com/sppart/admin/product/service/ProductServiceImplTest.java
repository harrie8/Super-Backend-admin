package com.sppart.admin.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.objectstorage.service.ObjectStorageService;
import com.sppart.admin.pictureinfo.domain.mapper.PictureInfoMapper;
import com.sppart.admin.pictureinfo.dto.RequestCreatePictureInfo;
import com.sppart.admin.product.domain.mapper.ProductMapper;
import com.sppart.admin.product.dto.ProductSearchCondition;
import com.sppart.admin.product.dto.request.RequestCreateProduct;
import com.sppart.admin.productwithtag.domain.mapper.ProductWithTagMapper;
import com.sppart.admin.tag.domain.entity.Tag;
import com.sppart.admin.tag.domain.mapper.TagMapper;
import com.sppart.admin.tag.dto.response.ResponseTag;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@ActiveProfiles("test")
@Sql({"classpath:truncate_table.sql", "classpath:test_data.sql"})
class ProductServiceImplTest {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductServiceImplTest(ProductMapper productMapper, PictureInfoMapper pictureInfoMapper,
                                  ProductWithTagMapper productWithTagMapper, TagMapper tagMapper) {
        this.productMapper = productMapper;
        this.productService = new ProductServiceImpl(productMapper,
                new ObjectStorageService() {
                    @Override
                    public String uploadFile(MultipartFile file) {
                        return "";
                    }

                    @Override
                    public List<String> uploadFiles(List<MultipartFile> files) {
                        return List.of();
                    }

                    @Override
                    public void delete(String url) {

                    }

                    @Override
                    public void delete(List<String> urls) {

                    }
                }, pictureInfoMapper, productWithTagMapper, tagMapper);
    }

    private final String[] extracting = {"productId", "title", "artistName", "tags", "pictureInfo.type", "price"};

    @Test
    @DisplayName("모든 작품 조회 테스트")
    void getProductsWithTagsByConditionTest() {
        //given
        var condition = ProductSearchCondition.builder()
                .build();

        //when
        var actual = productService.getProductsByCondition(condition).getFindDomains();

        //then
        assertThat(actual)
                .hasSize(29)
                .extracting("productId")
                .containsExactlyInAnyOrder(
                        1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L,
                        22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L
                );
    }

    @Test
    @DisplayName("작품명으로 작품 조회 테스트")
    void getProductsByTitleConditionTest() {
        //given
        var condition = ProductSearchCondition.builder()
                .title("roses")
                .build();

        //when
        var actual = productService.getProductsByCondition(condition).getFindDomains();

        //then
        assertThat(actual)
                .hasSize(1)
                .extracting(extracting)
                .containsExactlyInAnyOrder(
                        id1Product()
                );
    }

    @Test
    @DisplayName("작가 이름으로 작품 조회 테스트")
    void getProductsByArtistNameConditionTest() {
        //given
        var condition = ProductSearchCondition.builder()
                .artistName("문소")
                .build();

        //when
        var actual = productService.getProductsByCondition(condition).getFindDomains();

        //then
        assertThat(actual)
                .hasSize(12)
                .extracting(extracting)
                .containsExactlyInAnyOrder(
                        id1Product(),
                        id2Product(),
                        id3Product(),
                        id4Product(),
                        id5Product(),
                        id6Product(),
                        id7Product(),
                        id8Product(),
                        id9Product(),
                        id10Product(),
                        id11Product(),
                        id12Product()
                );
    }

    @Test
    @DisplayName("작품 코드로 작품 조회 테스트")
    void getProductsByProductIdConditionTest() {
        //given
        var condition = ProductSearchCondition.builder()
                .productId(1L)
                .build();

        //when
        var actual = productService.getProductsByCondition(condition).getFindDomains();

        //then
        assertThat(actual)
                .hasSize(1)
                .extracting(extracting)
                .containsExactlyInAnyOrder(
                        id1Product()
                );
    }

    @Test
    @DisplayName("작품 코드들로 작품들을 삭제하는 테스트")
    void bulkDeleteByIdsTest() {
        //given
        var ids = Set.of(1L, 2L);
        var beforeProductCount = productMapper.countAll();

        //when
        var actual = productService.bulkDeleteByIds(ids);

        //then
        var afterProductCount = productMapper.countAll();
        assertAll(() -> {
            assertEquals(ids.size(), actual.getExhibitionDeleteCount());
            assertEquals(beforeProductCount, afterProductCount + ids.size());
        });
    }

    @Test
    @DisplayName("작품 코드를 전달하지 않으면 작품을 삭제하지 않는 테스트")
    void bulkDeleteByNoIdsTest() {
        //given
        var ids = new HashSet<Long>();
        var beforeProductCount = productMapper.countAll();

        //when
        var actual = productService.bulkDeleteByIds(ids);

        //then
        var afterProductCount = productMapper.countAll();
        assertAll(() -> {
            assertEquals(ids.size(), actual.getProductExhibitionDeleteCount());
            assertEquals(ids.size(), actual.getExhibitionDeleteCount());
            assertEquals(beforeProductCount, afterProductCount);
        });
    }

    @Test
    @DisplayName("작품 상세 조회 시 작품 상세 정보, 태그, 전시 상태, 작품이 참여중인 전시 이름을 반환하는 테스트")
    void getDetailInfoByIdTest() {
        //given
        var productId = 1L;

        //when
        var actual = productService.getDetailInfoById(productId);

        //then
        assertAll(() -> {
            assertEquals(1L, actual.getProductId());
            assertEquals("6e305a70-0ed7-4f40-a59e-e28bb495887d.jpg", actual.getPicture());
            assertEquals("roses", actual.getTitle());
            assertEquals("문소", actual.getArtistName());
            assertEquals("나를 위로해주는 아름다운 장미, 그리고 음악과 함께 떠오르는 아련한 기억", actual.getDescription());
            assertEquals(250_000, actual.getPrice());
            assertEquals(21, actual.getBasicView());
            assertEquals(2, actual.getQrView());
            assertEquals(2, actual.getLikeCount());
            assertEquals(3, actual.getOrderCount());
            assertEquals("고급켄트지에 디지털프린팅", actual.getPictureInfo().getType());
            assertEquals("83X59cm (A1)", actual.getPictureInfo().getSize());
            assertEquals(2023, actual.getPictureInfo().getYear());
            assertThat(actual.getTags())
                    .hasSize(3)
                    .containsExactlyInAnyOrder(
                            Tag.builder()
                                    .tag_id(1L)
                                    .name("청량한")
                                    .build(),
                            Tag.builder()
                                    .tag_id(15L)
                                    .name("맑은")
                                    .build(),
                            Tag.builder()
                                    .tag_id(27L)
                                    .name("아련한")
                                    .build()
                    );
        });
    }

    //    @Test
    @DisplayName("전시에 참여하지 않은 작품 상세 조회 시 작품 상세 정보만 반환하는 테스트")
    void getDetailInfoByIdWithNotExistsParticipatedExhibitionsTest() {
        //given
        var productId = 5L;

        //when
        var actual = productService.getDetailInfoById(productId);

        //then
        assertAll(() -> {
            assertEquals(5L, actual.getProductId());
            assertEquals("72745895-04f3-4966-9263-932220f32e5f.jpg", actual.getPicture());
            assertEquals("Home sweet home", actual.getTitle());
            assertEquals("문소", actual.getArtistName());
            assertEquals("오늘의 힐링, 이보다 더 좋을 수 없어.", actual.getDescription());
            assertEquals(100_000, actual.getPrice());
            assertEquals(7, actual.getBasicView());
            assertEquals(4, actual.getQrView());
            assertEquals(1, actual.getLikeCount());
            assertEquals(1, actual.getOrderCount());
//            assertThat(actual.getTags())
//                    .hasSize(3)
//                    .containsExactlyInAnyOrder(
//                            "일상적인",
//                            "평온한",
//                            "컬러풀한"
//                    );
        });
    }

    @Test
    @DisplayName("존재하지 않는 작품 ID로 작품 상세 조회 시 예외 발생하는 테스트")
    void getDetailInfoByIdWithNotProductIdTest() {
        //given
        var notExistsProductId = 100L;

        //expected
        assertThrows(SuperpositionAdminException.class,
                () -> productService.getDetailInfoById(notExistsProductId));
    }

    @Test
    @DisplayName("작품 생성 테스트")
    void createTest() {
        //given
        var requestCreatePictureInfo = RequestCreatePictureInfo.builder()
                .type("고급켄트지에 디지털프린팅")
                .size("83X59cm (A1)")
                .year(2023)
                .build();
        var tagIds = Set.of(1L, 15L, 27L);
        var req = RequestCreateProduct.builder()
                .title("roses2")
                .artistName("문소2")
                .description("나를 위로해주는 아름다운 장미, 그리고 음악과 함께 떠오르는 아련한 기억2")
                .pictureInfo(requestCreatePictureInfo)
                .tagIds(tagIds)
                .price(250_000)
                .build();
        var picture = new MockMultipartFile("file", "test.txt", "text/plain",
                "test file".getBytes(StandardCharsets.UTF_8));

        //when
        var createdProductId = productService.create(req, picture);

        //then
        var actual = productMapper.findDetailProductInfoById(createdProductId).get();
        assertAll(() -> {
            assertNotNull(actual.getPicture());
            assertEquals(req.getTitle(), actual.getTitle());
            assertEquals(req.getArtistName(), actual.getArtistName());
            assertEquals(req.getDescription(), actual.getDescription());
            assertEquals(0, actual.getBasicView());
            assertEquals(0, actual.getQrView());
            assertEquals(0, actual.getLikeCount());
            assertEquals(0, actual.getOrderCount());
            assertEquals(req.getPictureInfo().getType(), actual.getPictureInfo().getType());
            assertEquals(req.getPictureInfo().getSize(), actual.getPictureInfo().getSize());
            assertEquals(req.getPictureInfo().getYear(), actual.getPictureInfo().getYear());
            assertEquals(Set.of(Tag.builder()
                            .tag_id(1L)
                            .name("청량한")
                            .build(),
                    Tag.builder()
                            .tag_id(15L)
                            .name("맑은")
                            .build(),
                    Tag.builder()
                            .tag_id(27L)
                            .name("아련한")
                            .build()), actual.getTags());
        });
    }

    @Test
    @DisplayName("작품 생성 시 태그 이름의 합이 11자를 넘어가면 예외 발생하는 테스트")
    @Transactional
    void createWithNotValidTagNameLengthSumTest() {
        //given
        var requestCreatePictureInfo = RequestCreatePictureInfo.builder()
                .type("고급켄트지에 디지털프린팅")
                .size("83X59cm (A1)")
                .year(2023)
                .build();
        var tagIds = Set.of(1L, 19L, 21L);
        var req = RequestCreateProduct.builder()
                .title("roses")
                .artistName("문소")
                .description("나를 위로해주는 아름다운 장미, 그리고 음악과 함께 떠오르는 아련한 기억")
                .pictureInfo(requestCreatePictureInfo)
                .tagIds(tagIds)
                .price(250_000)
                .build();
        var picture = new MockMultipartFile("file", "test.txt", "text/plain",
                "test file".getBytes(StandardCharsets.UTF_8));

        //expected
        assertThrows(SuperpositionAdminException.class, () -> productService.create(req, picture));
    }

    @Test
    @DisplayName("작품 생성 시 태그 ID가 1보다 작으면 예외 발생하는 테스트")
    @Transactional
    void createWithNotValidTagIdsTest() {
        //given
        var requestCreatePictureInfo = RequestCreatePictureInfo.builder()
                .type("고급켄트지에 디지털프린팅")
                .size("83X59cm (A1)")
                .year(2023)
                .build();
        var tagIds = Set.of(0L, 15L, 27L);
        var req = RequestCreateProduct.builder()
                .title("roses")
                .artistName("문소")
                .description("나를 위로해주는 아름다운 장미, 그리고 음악과 함께 떠오르는 아련한 기억")
                .pictureInfo(requestCreatePictureInfo)
                .tagIds(tagIds)
                .price(250_000)
                .build();
        var picture = new MockMultipartFile("file", "test.txt", "text/plain",
                "test file".getBytes(StandardCharsets.UTF_8));

        //expected
        assertThrows(SuperpositionAdminException.class, () -> productService.create(req, picture));
    }

    @Test
    @DisplayName("작품 생성 시 태그 값이 없으면 예외 발생하는 테스트")
    void createWithEmptyTagsTest() {
        //given
        var requestCreatePictureInfo = RequestCreatePictureInfo.builder()
                .type("고급켄트지에 디지털프린팅")
                .size("83X59cm (A1)")
                .year(2023)
                .build();
        var req = RequestCreateProduct.builder()
                .title("roses")
                .artistName("문소")
                .description("나를 위로해주는 아름다운 장미, 그리고 음악과 함께 떠오르는 아련한 기억")
                .pictureInfo(requestCreatePictureInfo)
                .tagIds(Set.of())
                .price(250_000)
                .build();
        var picture = new MockMultipartFile("file", "test.txt", "text/plain",
                "test file".getBytes(StandardCharsets.UTF_8));

        //expected
        assertThrows(SuperpositionAdminException.class, () -> productService.create(req, picture));
    }

    private Tuple id1Product() {
        return tuple(
                1L,
                "roses",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(1L)
                                .name("청량한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(15L)
                                .name("맑은")
                                .build(),
                        ResponseTag.builder()
                                .tagId(27L)
                                .name("아련한")
                                .build()),
                "고급켄트지에 디지털프린팅",
                250_000
        );
    }

    private Tuple id2Product() {
        return tuple(
                2L,
                "highway",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(1L)
                                .name("청량한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(2L)
                                .name("일상적인")
                                .build(),
                        ResponseTag.builder()
                                .tagId(23L)
                                .name("자유로운")
                                .build()),
                "고급켄트지에 디지털프린팅",
                350_000
        );
    }

    private Tuple id3Product() {
        return tuple(
                3L,
                "우리들",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(28L)
                                .name("키치한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(2L)
                                .name("일상적인")
                                .build()),
                "고급켄트지에 디지털프린팅",
                100000
        );
    }

    private Tuple id4Product() {
        return tuple(
                4L,
                "How Deep Is Your Love?",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(1L)
                                .name("청량한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(11L)
                                .name("컬러풀한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(29L)
                                .name("평화로운")
                                .build()),
                "고급켄트지에 디지털프린팅",
                100000
        );
    }

    private Tuple id5Product() {
        return tuple(
                5L,
                "Home sweet home",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(2L)
                                .name("일상적인")
                                .build(),
                        ResponseTag.builder()
                                .tagId(4L)
                                .name("평온한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(11L)
                                .name("컬러풀한")
                                .build()),
                "고급켄트지에 디지털프린팅",
                100000
        );
    }

    private Tuple id6Product() {
        return tuple(
                6L,
                "fall",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(3L)
                                .name("따뜻한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(4L)
                                .name("평온한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(8L)
                                .name("귀여운")
                                .build()),
                "고급켄트지에 디지털프린팅",
                250000
        );
    }

    private Tuple id7Product() {
        return tuple(
                7L,
                "flower child",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(4L)
                                .name("평온한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(8L)
                                .name("귀여운")
                                .build(),
                        ResponseTag.builder()
                                .tagId(11L)
                                .name("컬러풀한")
                                .build()),
                "고급켄트지에 디지털프린팅",
                200000
        );
    }

    private Tuple id8Product() {
        return tuple(
                8L,
                "summer2 (rainy day)",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(5L)
                                .name("시원한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(20L)
                                .name("상쾌한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(30L)
                                .name("즐거운")
                                .build()),
                "고급켄트지에 디지털프린팅",
                100000
        );
    }

    private Tuple id9Product() {
        return tuple(
                9L,
                "summer4 (icecream)",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(26L)
                                .name("달콤한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(11L)
                                .name("컬러풀한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(2L)
                                .name("일상적인")
                                .build()),
                "고급켄트지에 디지털프린팅",
                200000
        );
    }

    private Tuple id10Product() {
        return tuple(
                10L,
                "city light",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(7L)
                                .name("도시의")
                                .build(),
                        ResponseTag.builder()
                                .tagId(10L)
                                .name("빛나는")
                                .build(),
                        ResponseTag.builder()
                                .tagId(23L)
                                .name("자유로운")
                                .build()),
                "고급켄트지에 디지털프린팅",
                200000
        );
    }

    private Tuple id11Product() {
        return tuple(
                11L,
                "winter1 (with cat)",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(8L)
                                .name("귀여운")
                                .build(),
                        ResponseTag.builder()
                                .tagId(21L)
                                .name("잔망스러운")
                                .build()),
                "고급켄트지에 디지털프린팅",
                200000
        );
    }

    private Tuple id12Product() {
        return tuple(
                12L,
                "winter2 (with dog)",
                "문소",
                Set.of(
                        ResponseTag.builder()
                                .tagId(9L)
                                .name("포근한")
                                .build(),
                        ResponseTag.builder()
                                .tagId(19L)
                                .name("아기자기한")
                                .build()),
                "고급켄트지에 디지털프린팅",
                200000
        );
    }
}