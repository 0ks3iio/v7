package net.zdsoft.dataimport.biz;

/**
 * @author shenke
 * @since 2017.08.01
 */
public interface QImportEntity<Q extends QImportError> {

    Q createQImportError();
}
