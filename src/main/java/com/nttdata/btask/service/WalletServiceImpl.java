package com.nttdata.btask.service;

import com.nttdata.btask.interfaces.WalletService;
import com.nttdata.domain.contract.WalletRepository;
import com.nttdata.domain.models.WalletDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WalletServiceImpl implements WalletService {
  private final WalletRepository walletRepository;

  public WalletServiceImpl(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Override
  public Multi<WalletDto> list() {
    return walletRepository.list();
  }

  @Override
  public Uni<WalletDto> findByNroWallet(WalletDto walletDto) {
    return walletRepository.findByNroWallet(walletDto);
  }

  @Override
  public Uni<WalletDto> addWallet(WalletDto walletDto) {
    return walletRepository.addWallet(walletDto);
  }

  @Override
  public Uni<WalletDto> updateWallet(WalletDto walletDto) {
    return walletRepository.updateWallet(walletDto);
  }

  @Override
  public Uni<WalletDto> deleteWallet(WalletDto walletDto) {
    return walletRepository.deleteWallet(walletDto);
  }

  @Override
  public Uni<WalletDto> depositWallet(WalletDto walletDto) {
    return walletRepository.depositWallet(walletDto);
  }

  @Override
  public Uni<WalletDto> withdrawalWallet(WalletDto walletDto) {
    System.out.println(walletDto);
    return walletRepository.withdrawalWallet(walletDto);
  }
}
