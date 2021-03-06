package ua.com.ex.reprository.impl;

import org.springframework.stereotype.Repository;

import ua.com.ex.configuration.Path;

@Repository("imageCategoryRepository")
public class ImageCategoryRepository extends ImageRepositoryImpl {

    @Override
    protected String getLocalPath(int productId) {
        return Path.getLocalCategoryImagePath(productId);
    }

    @Override
    protected String getRemotePath(int id) {
        return Path.getRemoteCategoryImagePath(id);
    }
}
